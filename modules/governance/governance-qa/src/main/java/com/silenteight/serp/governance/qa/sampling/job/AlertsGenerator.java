package com.silenteight.serp.governance.qa.sampling.job;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.generator.AlertsGeneratorService;

import org.springframework.dao.DataIntegrityViolationException;

import java.time.OffsetDateTime;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Slf4j
class AlertsGenerator {

  @NonNull
  private final AlertSamplingByState samplingQuery;
  @NonNull
  private final AlertSamplingService alertSamplingService;
  @NonNull
  private final AlertsGeneratorService alertsGeneratorService;
  @NonNull
  private final DateRangeProvider dateRangeProvider;
  @NonNull
  private final CronExecutionTimeProvider cronExecutionTimeProvider;
  @NonNull
  private final TimeSource timeSource;

  public void generateAlertsIfNeeded() {
    OffsetDateTime startedAt = cronExecutionTimeProvider.executionTime();
    DateRangeDto dateRangeDto = dateRangeProvider.latestDateRange();
    log.debug("Generating alerts audit started at {} for date-range[{}]",
              timeSource.now(), dateRangeDto);
    alertSamplingService.failLongRunningTasks(startedAt);
    if (assertCanBeStarted(dateRangeDto))
      tryGenerateAlerts(dateRangeDto, startedAt);
    log.debug("Generating alerts audit finished at {}", timeSource.now());
  }

  private boolean assertCanBeStarted(DateRangeDto dateRangeDto) {
    return samplingQuery.listFinished(dateRangeDto).isEmpty();
  }

  private void tryGenerateAlerts(DateRangeDto dateRangeDto, OffsetDateTime startedAt) {
    log.info("Generating alerts started at {}", timeSource.now());
    try {
      Long alertsSamplingId = createAlertsSampling(dateRangeDto, startedAt);
      ofNullable(alertsSamplingId).ifPresent(id -> generateAlerts(dateRangeDto, id));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private Long createAlertsSampling(DateRangeDto dateRangeDto, OffsetDateTime startedAt) {
    Long alertsSamplingId = null;
    try {
      alertsSamplingId = alertSamplingService.createAlertsSampling(dateRangeDto, startedAt);
    } catch (DataIntegrityViolationException e) {
      log.warn("Could not start generating alerts - other instance already running");
    }
    return alertsSamplingId;
  }

  private void generateAlerts(DateRangeDto dateRangeDto, Long alertsSamplingId) {
    try {
      doGenerateAlerts(dateRangeDto, alertsSamplingId);
    } catch (RuntimeException e) {
      alertSamplingService.markAsFailed(alertsSamplingId);
      log.error(e.getMessage(), e);
    }
  }

  private void doGenerateAlerts(DateRangeDto dateRangeDto, Long alertsSamplingId) {
    alertsGeneratorService.generateAlerts(dateRangeDto, alertsSamplingId);
    alertSamplingService.finish(alertsSamplingId);
    log.info("Generating alerts finished at {}", timeSource.now());
  }
}
