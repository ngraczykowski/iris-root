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
      generateAlerts(dateRangeDto, startedAt);
    log.debug("Generating alerts audit finished at {}", timeSource.now());
  }

  private boolean assertCanBeStarted(DateRangeDto dateRangeDto) {
    return samplingQuery.listFinished(dateRangeDto).isEmpty();
  }

  private void generateAlerts(DateRangeDto dateRangeDto, OffsetDateTime startedAt) {
    log.info("Generating alerts started at {}", timeSource.now());
    Long alertsSamplingId = createAlertsSampling(dateRangeDto, startedAt);

    try {
      doGenerateAlerts(dateRangeDto, alertsSamplingId);
    } catch (RuntimeException e) {
      alertSamplingService.markAsFailed(alertsSamplingId);
      log.error(e.getMessage(), e);
    }
  }

  private Long createAlertsSampling(DateRangeDto dateRangeDto, OffsetDateTime startedAt) {
    Long alertsSamplingId = null;
    try {
      alertsSamplingId = alertSamplingService.createAlertsSampling(dateRangeDto, startedAt);
    } catch (DataIntegrityViolationException e) {
      log.debug("Could not start generating alerts - other instance already running");
    }
    return alertsSamplingId;
  }

  private void doGenerateAlerts(DateRangeDto dateRangeDto, Long alertsSamplingId) {
    alertsGeneratorService.generateAlerts(dateRangeDto);
    alertSamplingService.finish(alertsSamplingId);
    log.info("Generating alerts finished at {}", timeSource.now());
  }
}
