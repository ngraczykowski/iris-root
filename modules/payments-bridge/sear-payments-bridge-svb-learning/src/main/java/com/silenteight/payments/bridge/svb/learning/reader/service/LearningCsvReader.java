package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertsRead;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
class LearningCsvReader {

  private final CsvFileProvider csvFileProvider;
  private final CreateAlertUseCase createAlertUseCase;

  public AlertsRead read(Consumer<LearningAlert> alertConsumer) {
    InputStream streamCsv;
    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = mapper
        .schemaFor(LearningCsvRow.class)
        .withHeader()
        .withColumnSeparator(',');

    AlertsRead alertsRead;

    try {
      streamCsv = new FileInputStream(csvFileProvider.getLearningCsv());
      MappingIterator<LearningCsvRow> it = mapper
          .readerFor(LearningCsvRow.class)
          .with(schema)
          .readValues(streamCsv);
      alertsRead = readByAlerts(it, alertConsumer);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return alertsRead;
  }

  private AlertsRead readByAlerts(
      MappingIterator<LearningCsvRow> it, Consumer<LearningAlert> alertConsumer) {
    var firstRow = it.next();
    String currentAlertID = firstRow.getFkcoId();

    List<LearningCsvRow> alertRows = new ArrayList<>();
    alertRows.add(firstRow);

    int failedAlertsCount = 0;
    int successfulAlertsCount = 0;

    while (it.hasNext()) {
      var row = it.next();
      var rowAlertId = row.getFkcoId();

      if (currentAlertID.equals(rowAlertId)) {
        alertRows.add(row);
        continue;
      }

      currentAlertID = rowAlertId;
      try {
        alertConsumer.accept(createAlertUseCase.fromCsvRows(alertRows));
        successfulAlertsCount++;
      } catch (RuntimeException e) {
        log.error("Failed to process alert = {} reason = {}", rowAlertId, e.getMessage());
        failedAlertsCount++;
      }
      alertRows.clear();
    }

    return AlertsRead
        .builder()
        .failedAlerts(failedAlertsCount)
        .successfulAlerts(successfulAlertsCount)
        .build();
  }
}
