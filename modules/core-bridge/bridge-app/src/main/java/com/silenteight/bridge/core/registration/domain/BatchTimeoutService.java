package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertName;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.COMPLETED;
import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.DELIVERED;

@Service
@RequiredArgsConstructor
@Slf4j
class BatchTimeoutService {

  private static final EnumSet<BatchStatus> COMPLETION_STATUSES = EnumSet.of(COMPLETED, DELIVERED);

  private final BatchRepository batchRepository;
  private final AlertRepository alertRepository;
  private final BatchEventPublisher batchEventPublisher;

  void verifyBatchTimeout(VerifyBatchTimeoutCommand command) {
    batchRepository.findById(command.batchId())
        .ifPresentOrElse(
            batch -> validateStatus(batch)
                .ifPresentOrElse(
                    this::notifyBatchTimedOut,
                    () -> logBatchIsAlreadyCompleted(batch)
                ),
            () -> logBatchDoesNotExist(command)
        );
  }

  private Optional<Batch> validateStatus(Batch batch) {
    return Optional.of(batch)
        .filter(this::isNotCompleted);
  }

  private boolean isNotCompleted(Batch batch) {
    return !COMPLETION_STATUSES.contains(batch.status());
  }

  private void notifyBatchTimedOut(Batch batch) {
    log.info("Batch with id [{}] is timed out", batch.id());
    var alertNames = getAlertNames(batch.id());
    if (CollectionUtils.isNotEmpty(alertNames)) {
      var event = new BatchTimedOut(batch.analysisName(), alertNames);
      batchEventPublisher.publish(event);
    } else {
      log.info("No pending alerts found for batch with id: {}", batch.id());
    }
  }

  private List<String> getAlertNames(String batchId) {
    return alertRepository.findNamesByBatchIdAndStatusIsRegisteredOrProcessing(batchId).stream()
        .map(AlertName::alertName)
        .toList();
  }

  private void logBatchIsAlreadyCompleted(Batch batch) {
    log.info(
        "Ignoring batch with id [{}] because it is already completed with status: {}", batch.id(),
        batch.status());
  }

  private void logBatchDoesNotExist(VerifyBatchTimeoutCommand command) {
    log.warn("Ignoring batch with id [{}]. It does not exist", command.batchId());
  }
}
