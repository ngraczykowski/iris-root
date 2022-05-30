package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertToRetention;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobAlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.bridge.core.registration.domain.model.DataRetentionMode.DRY;

@Slf4j
@RequiredArgsConstructor
@Component
class DataRetentionDryRunStrategy implements DataRetentionStrategy {

  private final DataRetentionJobRepository jobRepository;
  private final DataRetentionJobAlertRepository jobAlertRepository;

  @Override
  public DataRetentionMode getSupportedDataRetentionMode() {
    return DRY;
  }

  @Override
  @Transactional
  public void run(DataRetentionStrategyCommand command) {
    var jobId = jobRepository.save(command.expirationDate(), command.mode());
    log.info("Created data retention dry run job with ID [{}]", jobId);

    if (command.alerts().isEmpty()) {
      log.info("No alerts qualified for dry data retention in job [{}]. Exiting.", jobId);
      return;
    }

    log.info("Storing [{}] alerts in dry run job [{}]", command.alerts().size(), jobId);
    jobAlertRepository.saveAll(jobId, extractAlertPrimaryIds(command.alerts()));
  }

  private List<Long> extractAlertPrimaryIds(List<AlertToRetention> alerts) {
    return alerts.stream()
        .map(AlertToRetention::alertPrimaryId)
        .toList();
  }
}
