package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(EtlAlertServiceProperties.class)
@Service
@RequiredArgsConstructor
@Slf4j
class LearningAlertBatchService {

  private final IngestService ingestService;
  private final EtlAlertServiceProperties properties;
  private final EtlAlertService etlAlertService;
  private final LearningErrorService learningErrorService;

  public boolean addToBatch(LearningAlertBatch currentBatch,
      List<LearningCsvRow> alertRows, boolean lastPass) {
    createLearningAlert(currentBatch, alertRows, currentBatch);
    if (lastPass || currentBatch.getCounter() >= properties.getBatchSize()) {
      triggerProcessing(currentBatch);
      return true;
    }
    return false;
  }

  private void createLearningAlert(LearningAlertBatch currentBatch,
      List<LearningCsvRow> alertRows, LearningAlertBatch batch) {
    try {
      var learningAlert = etlAlertService
          .fromCsvRows(alertRows)
          .batchStamp(currentBatch.getAlertMetaData().getBatchStamp())
          .fileName(currentBatch.getAlertMetaData().getFileName())
          .build();
      batch.addLearningAlert(learningAlert);
      log.debug("LearningAlert {} created successfully", learningAlert.getAlertId());
    } catch (RuntimeException e) {
      log.error("Failed to create LearningAlert = {} reason = {}",
          alertRows.get(0).getFkcoVSystemId(), e.getMessage(), e);
      batch.addError(ReadAlertError
          .builder()
          .alertId(alertRows.get(0).getFkcoVSystemId())
          .exception(e)
          .build());
    }
  }

  private void triggerProcessing(LearningAlertBatch currentBatch) {
    try {
      ingestService.ingest(currentBatch);

      log.info(
            "Learning alerts imported successfully: {}, details: {}",
            currentBatch.getSuccess().size(),
            collectAlertIds(currentBatch.getSuccess()));
      log.info("Failed to import learning alerts: {}, details: {}",
          currentBatch.getErrors().size(),
          collectErrorMessages(currentBatch.getErrors()));

      learningErrorService.save(currentBatch);

    } catch (Exception exception) {
      log.error("Batch creation of alerts and/or matches for alerts {} failed",
          collectAlertIds(currentBatch.getLearningAlerts()), exception);
    }
  }

  private String collectAlertIds(List<LearningAlert> alerts) {
    return alerts.stream()
        .map(LearningAlert::getAlertId).collect(Collectors.joining(","));
  }

  private String collectErrorMessages(List<ReadAlertError> errors) {
    return errors.stream()
        .map(ReadAlertError::toShortMessage).collect(Collectors.joining(","));
  }
}
