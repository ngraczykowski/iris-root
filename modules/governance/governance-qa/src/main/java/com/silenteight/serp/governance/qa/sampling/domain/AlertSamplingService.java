package com.silenteight.serp.governance.qa.sampling.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.domain.exception.WrongAlertSamplingIdException;

import java.time.OffsetDateTime;
import javax.transaction.Transactional;
import javax.validation.Valid;

import static com.silenteight.serp.governance.qa.sampling.domain.JobState.STARTED;
import static java.util.Collections.singletonList;


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

  public void finish(Long id) {
    AlertSampling alertSampling = getAlertSampling(id);
    alertSampling.finished(timeSource.offsetDateTime());
    alertSamplingRepository.save(alertSampling);
  }

  public void markAsFailed(Long id) {
    AlertSampling alertSampling = getAlertSampling(id);
    alertSampling.failed();
    alertSamplingRepository.save(alertSampling);
  }

  private AlertSampling getAlertSampling(Long id) {
    return alertSamplingRepository
        .getById(id)
        .orElseThrow(() -> new WrongAlertSamplingIdException(id));
  }

  public void failLongRunningTasks(OffsetDateTime currentStartingTime) {
    alertSamplingRepository
        .getByStates(singletonList(STARTED))
        .stream()
        .filter(alertSampling -> isLongRunning(alertSampling, currentStartingTime))
        .forEach(AlertSampling::failed);
  }

  private static boolean isLongRunning(
      AlertSampling alertSampling, OffsetDateTime currentStartingTime) {

    return alertSampling.getStartedAt().isBefore(currentStartingTime);
  }
}
