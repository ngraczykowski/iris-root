package com.silenteight.adjudication.engine.metrics.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.alerts.alert.AlertLabelDataAccess;

import com.google.common.util.concurrent.AtomicDouble;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.LABEL_NAME_SOURCE;
import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.LABEL_VALUE_LEARNING;
import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.LABEL_VALUE_SOLVING;
import static com.silenteight.adjudication.engine.alerts.alert.jdbc.AlertQueryConstants.SOLVING_ALERTS;

@Slf4j
@RequiredArgsConstructor
class LearningMetricsGauge implements MeterBinder {


  public static final String TAG_TYPE = "type";

  private final AlertLabelDataAccess alertLabelDataAccess;

  private final AtomicLong solvingAlertsCount;
  private final AtomicLong learningAlertsCount;
  // How many learning alerts has been received as solving alerts (SLI).
  private final AtomicLong learningInSolvingCounter;
  // How many solving has been received as learning alerts in csv file.
  private final AtomicLong solvingInLearningCounter;
  private final AtomicDouble learningToSolvingPercentage;

  private MeterRegistry meterRegistry;


  void registerGauge() {
    buildGauge(List.of(Tag.of(TAG_TYPE, "solving-alerts")), () -> solvingAlertsCount);
    buildGauge(List.of(Tag.of(TAG_TYPE, "learning-alerts")), () -> learningAlertsCount);
    buildGauge(List.of(Tag.of(TAG_TYPE, "learning-in-solving")), () -> learningInSolvingCounter);
    buildGauge(List.of(Tag.of(TAG_TYPE, "solving-in-learning")), () -> solvingInLearningCounter);
    buildGauge(
        List.of(Tag.of(TAG_TYPE, "learning-to-solving-percentage")),
        () -> learningToSolvingPercentage);
  }

  @Override
  public void bindTo(MeterRegistry registry) {
    this.meterRegistry = registry;
    registerGauge();
    recalculate();
  }

  @Scheduled(cron = "${ae.metrics.learning.gauge-cron}")
  void recalculate() {
    log.debug("Refresh Learning Gauge metrics");
    long solvingCount = getAlertByLabelCount(LABEL_VALUE_SOLVING);
    long learningCount = getAlertByLabelCount(LABEL_VALUE_LEARNING);

    learningInSolvingCounter.set(alertLabelDataAccess.countAlertsLearningInSolvingSet());
    solvingInLearningCounter.set(alertLabelDataAccess.countAlertsSolvingInLearningSet());
    learningToSolvingPercentage.set(calculatePercentage(learningCount, solvingCount));

    if (log.isDebugEnabled()) {
      log.debug("Gauge solving:{}, learning:{}, slo:{}", solvingAlertsCount.get(),
          learningAlertsCount.get(), learningToSolvingPercentage.get());
    }
  }

  private static double calculatePercentage(Long obtained, Long total) {
    if (total > 0) {
      return obtained * 100f / total;
    } else {
      return 0.0;
    }
  }

  private long getAlertByLabelCount(String labelValueLearning) {
    return alertLabelDataAccess.countByNameAndValue(
        LABEL_NAME_SOURCE,
        labelValueLearning);
  }

  private void buildGauge(List<Tag> tags, Supplier<Number> supplier) {
    Gauge.builder(SOLVING_ALERTS, supplier)
        .tags(tags)
        .register(meterRegistry);
  }
}
