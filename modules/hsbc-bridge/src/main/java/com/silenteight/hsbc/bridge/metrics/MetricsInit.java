package com.silenteight.hsbc.bridge.metrics;

import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.annotation.Timed;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class MetricsInit {

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "http_server_requests_recommend", histogram = true)
  public void receiveBatch() {
    log.debug("Metric http_server_requests_recommend initialized.");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "http_server_requests_learning", histogram = true)
  public void receiveLearningBatch() {
    log.debug("Metric http_server_requests_learning initialized.");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "recommendation_generated_on_recommendation", histogram = true)
  public void onRecommendation() {
    log.info("Metric recommendation_generated_on_recommendation initialized.");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "bulk_processor_try_to_process_solving_bulk", histogram = true)
  public void tryToProcessSolvingBulk() {
    log.info("Metric bulk_processor_try_to_process_solving_bulk initialized.");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "bulk_processor_try_to_process_learning_bulk", histogram = true)
  public void tryToProcessLearningBulk() {
    log.info("Metric bulk_processor_try_to_process_learning_bulk initialized.");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "alert_processor_pre_process_alerts_within_solving_bulk", histogram = true)
  public void preProcessAlertsWithinSolvingBulk() {
    log.info("Metric alert_processor_pre_process_alerts_within_solving_bulk initialized.");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Timed(value = "alert_processor_pre_process_alerts_within_learning_bulk", histogram = true)
  public void preProcessAlertsWithinLearningBulk() {
    log.info("Metric alert_processor_pre_process_alerts_within_learning_bulk initialized.");
  }
}
