package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertToRetention;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionAlertsExpiredEvent;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionPersonalInformationExpiredEvent;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobAlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionPublisher;

import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Component
class DataRetentionWetRunStrategy implements DataRetentionStrategy {

  private final DataRetentionJobRepository jobRepository;
  private final DataRetentionJobAlertRepository jobAlertRepository;
  private final AlertRepository alertRepository;
  private final DataRetentionPublisher dataRetentionPublisher;

  @Override
  public Set<DataRetentionType> getSupportedDataRetentionTypes() {
    return Set.of(DataRetentionType.PERSONAL_INFO_EXPIRED, DataRetentionType.ALERTS_EXPIRED);
  }

  @Override
  @Transactional
  public void run(DataRetentionStrategyCommand command) {
    var jobId = jobRepository.save(command.expirationDate(), command.type());
    log.info("Created data retention wet run job with ID [{}]", jobId);

    if (command.alerts().isEmpty()) {
      log.info("No alert qualified for wet data retention in job [{}]. Exiting.", jobId);
      return;
    }

    var alertPrimaryIds = extractAlertsPrimaryIds(command.alerts());

    log.info("Storing [{}] alerts in wet run job [{}]", alertPrimaryIds.size(), jobId);
    jobAlertRepository.saveAll(jobId, alertPrimaryIds);

    log.info("Marking [{}] alerts as archived", alertPrimaryIds.size());
    alertRepository.markAsArchivedAndClearMetadata(alertPrimaryIds);

    publishMessages(command.alerts(), command.chunkSize(), command.type());
  }

  private List<Long> extractAlertsPrimaryIds(List<AlertToRetention> alerts) {
    return alerts.stream()
        .map(AlertToRetention::alertPrimaryId)
        .toList();
  }

  private void publishMessages(
      List<AlertToRetention> alerts, int chunkSize, DataRetentionType type) {
    var counter = new AtomicInteger(0);
    StreamEx.of(alerts)
        .groupRuns((prev, next) -> counter.incrementAndGet() % chunkSize != 0)
        .forEach(chunk -> sendMessageByType(type, chunk));
  }

  private void sendMessageByType(DataRetentionType type, List<AlertToRetention> alerts) {
    log.info("Sending message of type [{}] with alerts count [{}]", type, alerts.size());
    if (DataRetentionType.ALERTS_EXPIRED == type) {
      dataRetentionPublisher.publish(new DataRetentionAlertsExpiredEvent(alerts));
    } else {
      dataRetentionPublisher.publish(new DataRetentionPersonalInformationExpiredEvent(alerts));
    }
  }
}
