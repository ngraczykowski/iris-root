package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
class LearningCsvReader {

  private final CsvFileProvider csvFileProvider;
  private final CreateAlertUseCase createAlertUseCase;

  void read(Consumer<LearningAlert> alertConsumer) {
    InputStream streamCsv;
    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = mapper
        .schemaFor(LearningCsvRow.class)
        .withHeader()
        .withColumnSeparator(',');

    try {
      streamCsv = new FileInputStream(csvFileProvider.getLearningCsv());
      MappingIterator<LearningCsvRow> it = mapper
          .readerFor(LearningCsvRow.class)
          .with(schema)
          .readValues(streamCsv);
      readByAlerts(it, alertConsumer);
      streamCsv.close();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  void readByAlerts(
      MappingIterator<LearningCsvRow> it, Consumer<LearningAlert> alertConsumer) {
    var firstRow = it.next();
    String currentAlertID = firstRow.getFkcoId();

    List<LearningCsvRow> alertRows = new ArrayList<>();
    alertRows.add(firstRow);

    while (it.hasNext()) {
      var row = it.next();
      var rowAlertId = row.getFkcoId();

      if (!currentAlertID.equals(rowAlertId)) {
        currentAlertID = rowAlertId;
        alertConsumer.accept(createAlertUseCase.fromCsvRows(alertRows));
        alertRows.clear();
      }

      alertRows.add(row);
    }
  }
}
