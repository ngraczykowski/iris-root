package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(EtlAlertServiceProperties.class)
@Service
@RequiredArgsConstructor
@Slf4j
class BatchAlertConsumer {

  private final IngestService ingestService;
  private final DataSourceIngestService dataSourceIngestService;
  private final EtlAlertServiceProperties properties;

  private final List<LearningAlert> alerts = new ArrayList<>(100);

  @Getter
  private int successfulAlertsCount = 0;

  public void accept(LearningAlert learningAlert, List<ReadAlertError> errors, Boolean force) {
    alerts.add(learningAlert);
    if (force || alerts.size() >= properties.getBatchSize()) {
      ingest(errors);
    }
  }

  private void ingest(List<ReadAlertError> errors) {
    try {
      ingestService.ingest(alerts);
      dataSourceIngestService.createValues(alerts, errors);
      if (log.isDebugEnabled()) {
        log.debug("Successfully processed alerts = {}", collectAlertIds());
      }

      var alertIds = alerts.stream().map(LearningAlert::getAlertId).collect(Collectors.toSet());
      var alertErrors = errors.stream().map(ReadAlertError::getAlertId).collect(Collectors.toSet());
      alertIds.removeAll(alertErrors);
      successfulAlertsCount += alertIds.size();

    } catch (Exception exception) {
      log.error("Batch creation of alerts and/or matches for alerts {} failed",
          collectAlertIds(), exception);
    } finally {
      alerts.clear();
    }
  }

  private String collectAlertIds() {
    return alerts.stream()
        .map(LearningAlert::getAlertId).collect(Collectors.joining(","));
  }

}
