package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.Getter;
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
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
class ProcessAlertService {

  private static final DateTimeFormatter STAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss");

  private final CsvFileProvider csvFileProvider;
  private final EtlAlertService etlAlertService;
  private final BatchAlertConsumer batchAlertConsumer;

  public AlertsReadingResponse read(LearningRequest learningRequest) {

    var mapper = new CsvMapper();
    var schema = mapper
        .schemaFor(LearningCsvRow.class)
        .withHeader()
        .withEscapeChar('"')
        .withColumnSeparator(',');

    return csvFileProvider.getLearningCsv(learningRequest, learningCsv -> {
      AlertsReadingResponse alertsReadingResponse = null;
      try (var inputStream =
          new InputStreamReader(learningCsv.getContent(), Charset.forName("CP1250"))) {
        MappingIterator<LearningCsvRow> it = mapper
            .readerFor(LearningCsvRow.class)
            .with(schema)
            .readValues(inputStream);
        alertsReadingResponse = readByAlerts(it, learningRequest.getObject());
        alertsReadingResponse.setObjectData(learningCsv);

        return alertsReadingResponse;
      } catch (Exception e) {
        log.error("There was a problem when processing alert: ", e);
      }
      return alertsReadingResponse;
    });
  }

  private AlertsReadingResponse readByAlerts(MappingIterator<LearningCsvRow> it, String fileName) {

    var firstRow = it.next();
    assertRowNotNull(firstRow);
    String currentAlertId = firstRow.getFkcoVSystemId();

    var alertMetaData = AlertMetaData.builder()
        .batchStamp(createBatchStamp()).fileName(fileName).build();
    var learningAlertCreator = new LearningAlertCreator(alertMetaData);

    var alertRows = new ArrayList<LearningCsvRow>();
    alertRows.add(firstRow);

    while (it.hasNext()) {
      var row = it.next();
      assertRowNotNull(row);

      var rowAlertId = row.getFkcoVSystemId();
      if (!currentAlertId.equals(rowAlertId)) {
        learningAlertCreator.create(alertRows, false);
        currentAlertId = rowAlertId;
        alertRows.clear();
      }
      alertRows.add(row);
    }

    if (!alertRows.isEmpty()) {
      learningAlertCreator.create(alertRows, true);
    }

    return AlertsReadingResponse
        .builder()
        .failedAlerts(learningAlertCreator.getErrors().size())
        .successfulAlerts(learningAlertCreator.getSuccessfulAlertsCount())
        .readAlertErrorList(learningAlertCreator.getErrors())
        .build();
  }

  @RequiredArgsConstructor
  private class LearningAlertCreator {

    private final AlertMetaData alertMetaData;

    @Getter private final List<ReadAlertError> errors = new ArrayList<>();
    @Getter private int successfulAlertsCount = 0;

    void create(List<LearningCsvRow> alertRows, boolean force) {
      var learningAlert = doCreate(alertMetaData, alertRows, errors);
      if (learningAlert.isPresent()) {
        batchAlertConsumer.accept(learningAlert.get(), force);
        successfulAlertsCount++;
      }
    }

    private Optional<LearningAlert> doCreate(
        AlertMetaData alertMetaData, List<LearningCsvRow> alertRows, List<ReadAlertError> errors) {
      try {
        var learningAlert = etlAlertService
            .fromCsvRows(alertRows)
            .batchStamp(alertMetaData.getBatchStamp())
            .fileName(alertMetaData.getFileName())
            .build();
        log.debug("LearningAlert {} created successfully", learningAlert.getAlertId());
        return Optional.of(learningAlert);
      } catch (RuntimeException e) {
        log.error("Failed to create LearningAlert = {} reason = {}",
            alertRows.get(0).getFkcoVSystemId(), e.getMessage(), e);
        errors.add(ReadAlertError
            .builder()
            .alertId(alertRows.get(0).getFkcoVSystemId())
            .exception(e)
            .build());
        return Optional.empty();
      }
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
