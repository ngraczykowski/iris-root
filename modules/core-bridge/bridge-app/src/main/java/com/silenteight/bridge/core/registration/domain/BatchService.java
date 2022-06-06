package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;
import com.silenteight.bridge.core.registration.domain.command.NotifyBatchErrorCommand;
import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand;
import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchStatisticsRepository;
import com.silenteight.bridge.core.registration.domain.strategy.BatchStrategyFactory;

import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class BatchService {

  private static final EnumSet<BatchStatus> ALLOWED_BATCH_STATUSES_FOR_MARKING_AS_DELIVERED =
      EnumSet.of(BatchStatus.COMPLETED, BatchStatus.DELIVERED, BatchStatus.ERROR);
  private static final EnumSet<BatchStatus> ALLOWED_BATCH_STATUSES_FOR_REGISTRATION =
      EnumSet.of(BatchStatus.STORED, BatchStatus.PROCESSING);

  private final BatchEventPublisher eventPublisher;
  private final BatchRepository batchRepository;
  private final BatchStrategyFactory batchStrategyFactory;
  private final BatchStatisticsRepository statisticsRepository;

  BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchRepository.findById(registerBatchCommand.id())
        .map(this::logIfAlreadyExists)
        .map(batch -> validateBatchStatus(batch, ALLOWED_BATCH_STATUSES_FOR_REGISTRATION))
        .or(() -> registerNew(registerBatchCommand))
        .map(BatchId::from)
        .orElseThrow();
  }

  Batch findBatchByAnalysisName(String analysisName) {
    return batchRepository.findBatchByAnalysisName(analysisName)
        .orElseThrow();
  }

  Batch findBatchById(String batchId) {
    return batchRepository.findById(batchId)
        .orElseThrow();
  }

  BatchIdWithPolicy findBatchIdWithPolicyByAnalysisName(String analysisName) {
    return batchRepository.findBatchIdWithPolicyByAnalysisName(analysisName)
        .orElseThrow();
  }

  BatchPriorityWithStatus findPendingBatch(String batchId) {
    var batchProjection = batchRepository.findBatchPriorityById(batchId)
        .orElseThrow();

    var batch = Batch.builder()
        .id(batchId)
        .status(batchProjection.status())
        .build();

    validateBatchStatus(batch, ALLOWED_BATCH_STATUSES_FOR_REGISTRATION);

    return batchProjection;
  }

  BatchPriority findBatchPriority(String analysisName) {
    return batchRepository.findBatchPriorityByAnalysisName(analysisName);
  }

  void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchRepository.findById(notifyBatchErrorCommand.id())
        .ifPresentOrElse(
            batch -> markBatchAsError(notifyBatchErrorCommand),
            () -> registerNewAsError(notifyBatchErrorCommand));
    eventPublisher.publish(
        BatchError.builder()
            .id(notifyBatchErrorCommand.id())
            .batchMetadata(notifyBatchErrorCommand.batchMetadata())
            .errorDescription(notifyBatchErrorCommand.errorDescription())
            .build()
    );
  }

  void completeBatch(Batch batch) {
    batchStrategyFactory.getStrategyForCompletion(batch).completeBatch(batch);
  }

  void markBatchAsDelivered(Batch batch) {
    validateBatchStatus(batch, ALLOWED_BATCH_STATUSES_FOR_MARKING_AS_DELIVERED);

    log.info(
        "Set solving batch status to [{}] with batch id [{}].", BatchStatus.DELIVERED, batch.id());
    batchRepository.updateStatusToDelivered(batch.id());
    publishBatchDelivered(batch);
  }

  Map<BatchWithAlertsDto.AlertStatus, Long> getAlertsStatusStatistics(String batchId) {
    return statisticsRepository.getAlertsStatusStatistics(batchId);
  }

  Map<BatchWithAlertsDto.AlertStatus, Long> getAlertsStatusStatistics(
      String batchId, List<String> alertsNames) {
    return statisticsRepository.getAlertsStatusStatistics(batchId, alertsNames);
  }

  private Batch validateBatchStatus(Batch batch, EnumSet<BatchStatus> allowedStatuses) {
    if (!allowedStatuses.contains(batch.status())) {
      var message = String.format("Batch with batch id [%s] failed due to status validation."
              + " [%s] status is invalid, one of %s expected.",
          batch.id(), batch.status(), allowedStatuses);
      log.error(message);
      throw new IllegalStateException(message);
    }
    return batch;
  }

  private Batch logIfAlreadyExists(Batch batch) {
    log.info("Batch for the given batch id [{}] already exists.", batch.id());
    return batch;
  }

  private Optional<Batch> registerNew(RegisterBatchCommand registerBatchCommand) {
    return Optional.of(batchStrategyFactory
        .getStrategyForRegistration(registerBatchCommand)
        .register(registerBatchCommand));
  }

  private void markBatchAsError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    log.info("Set batch status to [{}] with batch id [{}] and set error description [{}].",
        BatchStatus.ERROR, notifyBatchErrorCommand.id(),
        notifyBatchErrorCommand.errorDescription());

    batchRepository.updateStatusAndErrorDescription(
        notifyBatchErrorCommand.id(),
        BatchStatus.ERROR,
        notifyBatchErrorCommand.errorDescription());
  }

  private void registerNewAsError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    log.info(
        "Registering new batch as [{}] with id [{}] and error description [{}].",
        BatchStatus.ERROR,
        notifyBatchErrorCommand.id(),
        notifyBatchErrorCommand.errorDescription());

    batchRepository.create(Batch.error(
        notifyBatchErrorCommand.id(),
        notifyBatchErrorCommand.errorDescription(),
        notifyBatchErrorCommand.batchMetadata(),
        notifyBatchErrorCommand.isSimulation()));

    log.info("New batch registered as error with batch id [{}].", notifyBatchErrorCommand.id());
  }

  private void publishBatchDelivered(Batch batch) {
    eventPublisher.publish(new BatchDelivered(batch.id(), batch.analysisName()));
  }
}
