package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.*;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.stereotype.Service;

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
        .withColumnSeparator(',');

    return csvFileProvider.getLearningCsv(learningRequest, learningCsv -> {
      try {
        MappingIterator<LearningCsvRow> it = mapper
            .readerFor(LearningCsvRow.class)
            .with(schema)
            .readValues(learningCsv.getContent());

        var alertsReadingResponse = readByAlerts(it, alertConsumer);
        alertsReadingResponse.setObjectData(learningCsv);

        return alertsReadingResponse;
      } catch (Exception e) {
        throw new ReadAlertException(e);
      }
    });
  }

  private AlertsReadingResponse readByAlerts(
      MappingIterator<LearningCsvRow> it, Consumer<LearningAlert> alertConsumer) {
    var firstRow = it.next();
    String currentAlertID = firstRow.getFkcoVSystemId();

    List<ReadAlertError> errors = new ArrayList<>();
    List<LearningCsvRow> alertRows = new ArrayList<>();
    alertRows.add(firstRow);

    int failedAlertsCount = 0;
    int successfulAlertsCount = 0;

    while (it.hasNext()) {
      var row = it.next();
      var rowAlertId = row.getFkcoVSystemId();

      if (currentAlertID.equals(rowAlertId)) {
        alertRows.add(row);
        continue;
      }

      currentAlertID = rowAlertId;
      try {
        alertConsumer.accept(createLearningAlertUseCase.fromCsvRows(alertRows));
        successfulAlertsCount++;
      } catch (RuntimeException e) {
        log.error("Failed to process alert = {} reason = {}", rowAlertId, e.getMessage());
        errors.add(ReadAlertError.builder().alertId(rowAlertId).exception(e).build());
        failedAlertsCount++;
      }
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

  private static class ReadAlertException extends RuntimeException {

    private static final long serialVersionUID = 7691761705445879166L;

    ReadAlertException(Exception e) {
      super(e);
    }
  }
}
