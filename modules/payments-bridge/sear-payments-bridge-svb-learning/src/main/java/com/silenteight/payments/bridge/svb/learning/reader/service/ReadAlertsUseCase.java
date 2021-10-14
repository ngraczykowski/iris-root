package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.*;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
class ReadAlertsUseCase {

  private final CsvFileProvider csvFileProvider;
  private final CreateLearningAlertUseCase createLearningAlertUseCase;

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
        alertsReadingResponse = readByAlerts(it, alertConsumer);
        alertsReadingResponse.setObjectData(learningCsv);

        return alertsReadingResponse;
      } catch (Exception e) {
        log.error("There was a problem when processing alert: ", e);
      }
      return alertsReadingResponse;
    });
  }

  private AlertsReadingResponse readByAlerts(
      MappingIterator<LearningCsvRow> it, Consumer<LearningAlert> alertConsumer) {
    var firstRow = it.next();
    assertRowNotNull(firstRow);
    String currentAlertID = firstRow.getFkcoVSystemId();

    List<ReadAlertError> errors = new ArrayList<>();
    List<LearningCsvRow> alertRows = new ArrayList<>();
    alertRows.add(firstRow);

    int failedAlertsCount = 0;
    int successfulAlertsCount = 0;

    while (it.hasNext()) {
      var row = it.next();
      assertRowNotNull(row);
      var rowAlertId = row.getFkcoVSystemId();

      if (currentAlertID.equals(rowAlertId)) {
        alertRows.add(row);
        continue;
      }

      try {
        alertConsumer.accept(createLearningAlertUseCase.fromCsvRows(alertRows));
        log.debug("Successfully processed alert = {}", currentAlertID);
        successfulAlertsCount++;
      } catch (RuntimeException e) {
        log.error("Failed to process alert = {} reason = {}", rowAlertId, e.getMessage());
        errors.add(ReadAlertError.builder().alertId(rowAlertId).exception(e).build());
        failedAlertsCount++;
      }
      currentAlertID = rowAlertId;
      alertRows.clear();
      alertRows.add(row);
    }

    return AlertsReadingResponse
        .builder()
        .failedAlerts(failedAlertsCount)
        .successfulAlerts(successfulAlertsCount)
        .readAlertErrorList(errors)
        .build();
  }

  static void assertRowNotNull(LearningCsvRow row) {
    if (row.getFkcoVSystemId() == null || row.getFkcoVSystemId().equals("")) {
      throw new ReadAlertException("Received empty row");
    }
  }

  private static class ReadAlertException extends RuntimeException {

    private static final long serialVersionUID = 7691761705445879166L;

    ReadAlertException(Exception e) {
      super(e);
    }

    ReadAlertException(String message) {
      super(message);
    }
  }
}
