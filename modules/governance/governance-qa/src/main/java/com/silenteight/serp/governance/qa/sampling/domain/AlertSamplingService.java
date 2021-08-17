package com.silenteight.serp.governance.qa.sampling.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.domain.exception.WrongAlertSamplingIdException;
import com.silenteight.serp.governance.qa.sampling.generator.dto.AlertDistributionDto;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.OffsetDateTime;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;

import static com.silenteight.serp.governance.qa.sampling.domain.JobState.STARTED;
import static java.util.Collections.singletonList;

@Slf4j
@RequiredArgsConstructor
public class AlertSamplingService {

  @NonNull
  private final AlertSamplingRepository alertSamplingRepository;
  @NonNull
  private final TimeSource timeSource;

  @Transactional
  public Long createAlertsSampling(
      @Valid DateRangeDto dateRangeDto, OffsetDateTime startedAt) {
    AlertSampling alertSampling = AlertSampling.of(dateRangeDto, startedAt);
    return alertSamplingRepository.save(alertSampling).getId();
  }

  @Transactional
  public void finish(Long id) {
    AlertSampling alertSampling = getAlertSampling(id);
    alertSampling.finished(timeSource.offsetDateTime());
    alertSamplingRepository.save(alertSampling);
    log.debug("Alert sampling with state={} saved", alertSampling.getState());
  }

  @Transactional
  public void markAsFailed(Long id) {
    AlertSampling alertSampling = getAlertSampling(id);
    alertSampling.failed();
    alertSamplingRepository.save(alertSampling);
    log.debug("Alert sampling with state={} saved", alertSampling.getState());
  }

  private AlertSampling getAlertSampling(Long id) {
    return alertSamplingRepository
        .getById(id)
        .orElseThrow(() -> new WrongAlertSamplingIdException(id));
  }

  public void failLongRunningTasks(OffsetDateTime currentStartingTime) {
    alertSamplingRepository
        .getAllByStateIn(singletonList(STARTED))
        .stream()
        .filter(alertSampling -> isLongRunning(alertSampling, currentStartingTime))
        .map(AlertSampling::getId)
        .forEach(this::markAsFailed);
  }

  public void saveAlertDistribution(
      Long id, List<AlertDistributionDto> distributions, Integer alertsCount) {

    AlertSampling alertSampling = getAlertSampling(id);
    try {
      String json = JsonConversionHelper.INSTANCE.objectMapper().writeValueAsString(distributions);
      alertSampling.setAlertsCount(alertsCount);
      alertSampling.setAlertsDistribution(json);
      alertSamplingRepository.save(alertSampling);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
  }

  private static boolean isLongRunning(
      AlertSampling alertSampling, OffsetDateTime currentStartingTime) {

    return alertSampling.getStartedAt().isBefore(currentStartingTime);
  }
}
