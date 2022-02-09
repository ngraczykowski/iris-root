package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertMetaData;
import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.learning.reader.port.CsvFileProvider;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
class ProcessAlertService {

  private static final DateTimeFormatter STAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd'-'HHmmss");

  private final CsvFileProvider csvFileProvider;
  private final LearningAlertBatchService learningAlertBatchService;

  public AlertsReadingResponse read(FileRequest fileRequest) {

    var mapper = new CsvMapper();
    var schema = CsvSchema.emptySchema()
        .withHeader()
        .withEscapeChar('"')
        .withColumnSeparator(',');

    return csvFileProvider.getLearningCsv(fileRequest, learningCsv -> {
      AlertsReadingResponse alertsReadingResponse = null;
      try (
          var inputStream =
              new InputStreamReader(learningCsv.getContent(), Charset.forName("CP1250"))) {
        MappingIterator<LearningCsvRow> it = mapper
            .readerFor(LearningCsvRow.class)
            .with(schema)
            .readValues(inputStream);
        alertsReadingResponse = readByAlerts(it, fileRequest.getObject());
        alertsReadingResponse.setObjectData(learningCsv);

        return alertsReadingResponse;
      } catch (Exception e) {
        log.error("There was a problem when reading learning file: ", e);
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

    var currentBatch = new LearningAlertBatch(alertMetaData);

    var alertRows = new ArrayList<LearningCsvRow>();
    alertRows.add(firstRow);

    int successfulAlertsCount = 0;
    var errors = new ArrayList<ReadAlertError>();

    while (it.hasNext()) {
      var row = it.next();
      assertRowNotNull(row);

      var rowAlertId = row.getFkcoVSystemId();
      if (!currentAlertId.equals(rowAlertId)) {
        if (learningAlertBatchService.addToBatch(currentBatch, alertRows, false)) {
          successfulAlertsCount += currentBatch.getSuccess().size();
          errors.addAll(currentBatch.getErrors());
          currentBatch = new LearningAlertBatch(alertMetaData);

        }
        currentAlertId = rowAlertId;
        alertRows.clear();
      }
      alertRows.add(row);
    }

    if (!alertRows.isEmpty()) {
      learningAlertBatchService.addToBatch(currentBatch, alertRows, true);
      successfulAlertsCount += currentBatch.getSuccess().size();
      errors.addAll(currentBatch.getErrors());
    }

    return AlertsReadingResponse
        .builder()
        .failedAlerts(errors.size())
        .successfulAlerts(successfulAlertsCount)
        .readAlertErrorList(errors)
        .build();
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
