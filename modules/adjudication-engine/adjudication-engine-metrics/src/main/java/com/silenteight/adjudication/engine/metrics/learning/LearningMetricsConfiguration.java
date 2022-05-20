package com.silenteight.adjudication.engine.metrics.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.alerts.alert.AlertLabelDataAccess;

import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

@Configuration
@Slf4j
@RequiredArgsConstructor
class LearningMetricsConfiguration {

  private final AlertLabelDataAccess alertLabelDataAccess;

  @Bean
  LearningMetricsGauge solvingAlertsCounterMetrics() {
    return new LearningMetricsGauge(alertLabelDataAccess,
        new AtomicLong(0), new AtomicLong(0),
        new AtomicLong(0), new AtomicLong(0),
        new AtomicDouble(0.0));
  }

}
