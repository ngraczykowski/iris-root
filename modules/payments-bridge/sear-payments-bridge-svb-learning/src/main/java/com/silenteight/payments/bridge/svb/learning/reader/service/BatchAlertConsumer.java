package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@EnableConfigurationProperties(EtlAlertServiceProperties.class)
@Service
@RequiredArgsConstructor
@Slf4j
class BatchAlertConsumer implements BiConsumer<LearningAlert, Boolean> {

  private final IngestService ingestService;
  private final EtlAlertServiceProperties properties;

  private final List<LearningAlert> alerts = new ArrayList<>(100);

  @Override
  public void accept(LearningAlert learningAlert, Boolean force) {
    alerts.add(learningAlert);
    if (force || alerts.size() >= properties.getBatchSize()) {
      ingest();
    }
  }

  private void ingest() {
    try {
      ingestService.ingest(alerts);
      if (log.isDebugEnabled()) {
        log.debug("Successfully processed alerts = {}", collectAlertIds());
      }

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
