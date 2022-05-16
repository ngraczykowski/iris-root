package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertToRetention;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobAlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.ALERTS_EXPIRED_DRY_RUN;
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.PERSONAL_INFO_EXPIRED_DRY_RUN;

@Slf4j
@RequiredArgsConstructor
@Component
class DataRetentionDryRunStrategy implements DataRetentionStrategy {

  private final DataRetentionJobRepository jobRepository;
  private final DataRetentionJobAlertRepository jobAlertRepository;

  @Override
  public Set<DataRetentionType> getSupportedDataRetentionTypes() {
    return Set.of(PERSONAL_INFO_EXPIRED_DRY_RUN, ALERTS_EXPIRED_DRY_RUN);
  }

  @Override
  @Transactional
  public void run(DataRetentionStrategyCommand command) {
    var jobId = jobRepository.save(command.expirationDate(), command.type());
    log.info("Created data retention job with ID [{}]", jobId);

    if (command.alerts().isEmpty()) {
      log.info("No alerts qualified for data retention in job [{}]. Exiting.", jobId);
      return;
    }

    var alertNames = extractAlertNames(command.alerts());
    log.info("Storing [{}] alert names in job [{}]", alertNames.size(), jobId);
    jobAlertRepository.saveAll(jobId, alertNames);
  }

  private List<String> extractAlertNames(List<AlertToRetention> alerts) {
    return alerts.stream()
        .map(AlertToRetention::name)
        .toList();
  }
}
