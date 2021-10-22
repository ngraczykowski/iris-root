package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.*;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
class ProcessAlertService {

  private static final DateTimeFormatter STAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss");

  private final CsvFileProvider csvFileProvider;
  private final EtlAlertService etlAlertService;

  public AlertsReadingResponse read(
      LearningRequest learningRequest, Consumer<LearningAlert> alertConsumer) {

    var mapper = new CsvMapper();
    var schema = mapper
        .schemaFor(LearningCsvRow.class)
        .withHeader()
        .withEscapeChar('"')
        .withColumnSeparator(',');

    return csvFileProvider.getLearningCsv(learningRequest, learningCsv -> {
      AlertsReadingResponse alertsReadingResponse = null;
      try {
        MappingIterator<LearningCsvRow> it = mapper
            .readerFor(LearningCsvRow.class)
            .with(schema)
            .readValues(new InputStreamReader(learningCsv.getContent(), Charset.forName("CP1250")));
        alertsReadingResponse = readByAlerts(it, alertConsumer, learningRequest.getObject());
        alertsReadingResponse.setObjectData(learningCsv);

        return alertsReadingResponse;
      } catch (Exception e) {
        log.error("There was a problem when processing alert: ", e);
      }
      return alertsReadingResponse;
    });
  }

  private AlertsReadingResponse readByAlerts(
      MappingIterator<LearningCsvRow> it, Consumer<LearningAlert> alertConsumer, String fileName) {

    var firstRow = it.next();
    assertRowNotNull(firstRow);
    String currentAlertId = firstRow.getFkcoVSystemId();

    var errors = new ArrayList<ReadAlertError>();
    var alertRows = new ArrayList<LearningCsvRow>();
    alertRows.add(firstRow);

    int successfulAlertsCount = 0;

    var batchStamp = createBatchStamp();

    while (it.hasNext()) {
      var row = it.next();
      assertRowNotNull(row);
      var rowAlertId = row.getFkcoVSystemId();

      if (currentAlertId.equals(rowAlertId)) {
        alertRows.add(row);
        continue;
      }

      var optError = proccessAlert(
          alertConsumer,
          AlertMetaData.builder().batchStamp(batchStamp).fileName(fileName).build(),
          alertRows);

      if (optError.isPresent())
        errors.add(optError.get());
      else
        successfulAlertsCount++;

      currentAlertId = rowAlertId;
      alertRows.clear();
      alertRows.add(row);
    }

    if (!alertRows.isEmpty()) {
      var optError = proccessAlert(
          alertConsumer,
          AlertMetaData.builder().batchStamp(batchStamp).fileName(fileName).build(),
          alertRows);

      if (optError.isPresent())
        errors.add(optError.get());
      else
        successfulAlertsCount++;
    }

    return AlertsReadingResponse
        .builder()
        .failedAlerts(errors.size())
        .successfulAlerts(successfulAlertsCount)
        .readAlertErrorList(errors)
        .build();
  }

  private Optional<ReadAlertError> proccessAlert(
      Consumer<LearningAlert> alertConsumer, AlertMetaData alertMetaData,
      ArrayList<LearningCsvRow> alertRows) {
    try {
      var alert = etlAlertService
          .fromCsvRows(alertRows)
          .batchStamp(alertMetaData.getBatchStamp())
          .fileName(alertMetaData.getFileName())
          .build();

      alertConsumer.accept(alert);
      log.debug("Successfully processed alert = {}", alert.getAlertId());
      return Optional.empty();
    } catch (RuntimeException e) {
      log.error(
          "Failed to process alert = {} reason = {}", alertRows.get(0).getFkcoVSystemId(),
          e.getMessage(), e);
      return Optional.of(ReadAlertError
          .builder()
          .alertId(alertRows.get(0).getFkcoVSystemId())
          .exception(e)
          .build());
    }
  }

  static void assertRowNotNull(LearningCsvRow row) {
    if (row.getFkcoVSystemId() == null || row.getFkcoVSystemId().equals("")) {
      throw new ReadAlertException("Received empty row");
    }
  }

  private static String createBatchStamp() {
    return STAMP_FORMATTER.format(OffsetDateTime.now(Clock.systemUTC())) + "-"
        + RandomStringUtils.randomAlphabetic(7);
  }

  private static class ReadAlertException extends RuntimeException {

    private static final long serialVersionUID = 7691761705445879166L;

    ReadAlertException(String message) {
      super(message);
    }
  }
}
