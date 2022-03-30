package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertName;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.COMPLETED;
import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.DELIVERED;
import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.PROCESSING;
import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.STORED;

@Service
@RequiredArgsConstructor
@Slf4j
class BatchTimeoutService {

  private static final EnumSet<BatchStatus> COMPLETION_STATUSES = EnumSet.of(COMPLETED, DELIVERED);
  private static final EnumSet<BatchStatus> PENDING_STATUSES = EnumSet.of(STORED, PROCESSING);

  private final BatchRepository batchRepository;
  private final AlertRepository alertRepository;
  private final BatchEventPublisher batchEventPublisher;

  void verifyBatchTimeout(VerifyBatchTimeoutCommand command) {
    withExistingBatch(command, batch ->
        validateStatus(batch, isNotCompleted())
            .ifPresentOrElse(
                this::notifyBatchTimedOut,
                () -> logBatchIsAlreadyCompleted(batch)
            )
    );
  }

  void verifyBatchTimeoutForAllErroneousAlerts(VerifyBatchTimeoutCommand command) {
    withExistingBatch(command, batch -> validateStatus(batch, isPending())
        .ifPresentOrElse(
            this::completeBatchIfAllAlertsAreErroneous,
            () -> logNotInStoredStatus(batch)
        )
    );
  }

  private void withExistingBatch(VerifyBatchTimeoutCommand command, Consumer<Batch> consumer) {
    batchRepository.findById(command.batchId())
        .ifPresentOrElse(consumer, () -> logBatchDoesNotExist(command));
  }

  private Optional<Batch> validateStatus(Batch batch, Predicate<Batch> predicate) {
    return Optional.of(batch)
        .filter(predicate);
  }

  private Predicate<Batch> isNotCompleted() {
    return batch -> !COMPLETION_STATUSES.contains(batch.status());
  }

  private Predicate<Batch> isPending() {
    return batch -> PENDING_STATUSES.contains(batch.status());
  }

  private void completeBatchIfAllAlertsAreErroneous(Batch batch) {
    var batchId = batch.id();
    var erroneousAlerts = alertRepository.countAllErroneousAlerts(batchId);
    if (erroneousAlerts == batch.alertsCount()) {
      batchRepository.updateStatusToCompleted(batchId);
      log.info("Batch [{}] marked as completed because all alerts are erroneous", batchId);
      batchEventPublisher.publish(buildBatchCompletedEvent(batch));
    }
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

  private BatchCompleted buildBatchCompletedEvent(Batch batch) {
    return BatchCompleted.builder()
        .id(batch.id())
        .analysisId(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .build();
  }

  private void logBatchIsAlreadyCompleted(Batch batch) {
    log.info(
        "Ignoring batch with id [{}] because it is already completed with status: {}", batch.id(),
        batch.status());
  }

  private void logNotInStoredStatus(Batch batch) {
    log.info("Ignoring batch with id [{}] because it is not in STORED status: {}",
        batch.id(), batch.status());
  }

  private void logBatchDoesNotExist(VerifyBatchTimeoutCommand command) {
    log.warn("Ignoring batch with id [{}]. It does not exist", command.batchId());
  }
}
