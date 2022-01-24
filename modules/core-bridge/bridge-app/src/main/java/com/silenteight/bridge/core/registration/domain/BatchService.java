package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class BatchService {

  private final EventPublisher eventPublisher;
  private final AnalysisService analysisService;
  private final BatchRepository batchRepository;
  private final DefaultModelService defaultModelService;
  private final BatchStatisticsService batchStatisticsService;

  BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchRepository.findById(registerBatchCommand.id())
        .map(this::logIfAlreadyExists)
        .or(() -> registerNew(registerBatchCommand))
        .map(BatchId::from)
        .orElseThrow();
  }

  BatchId findBatchId(String analysisName) {
    return batchRepository.findBatchIdByAnalysisName(analysisName)
        .orElseThrow();
  }

  BatchIdWithPolicy findBatchIdWithPolicyByAnalysisName(String analysisName) {
    return batchRepository.findBatchIdWithPolicyByAnalysisName(analysisName)
        .orElseThrow();
  }

  void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    var batchErrorStatistics = batchStatisticsService.createBatchErrorStatistics();
    batchRepository.findById(notifyBatchErrorCommand.id())
        .ifPresentOrElse(
            batch -> markBatchAsError(notifyBatchErrorCommand),
            () -> registerNewAsError(notifyBatchErrorCommand));
    eventPublisher.publish(
        BatchError.builder()
            .id(notifyBatchErrorCommand.id())
            .batchMetadata(notifyBatchErrorCommand.batchMetadata())
            .errorDescription(notifyBatchErrorCommand.errorDescription())
            .statistics(batchErrorStatistics)
            .build()
    );
  }

  void completeBatch(CompleteBatchCommand completeBatchCommand) {
    batchRepository.findById(completeBatchCommand.id())
        .ifPresentOrElse(
            batch -> {
              markBatchAsCompleted(batch.id());
              publishBatchCompleted(batch, completeBatchCommand.alertNames());
            },
            () -> log.error(
                "No batch found for batch id: {}",
                completeBatchCommand.id()));
  }

  void markBatchAsDelivered(String batchId) {
    validateBatchStatus(batchId);

    log.info("Set batch status to DELIVERED with batch id: {}", batchId);
    batchRepository.updateStatusToDelivered(batchId);
  }

  private void validateBatchStatus(String batchId) {
    var batch = findBatch(batchId);

    if (BatchStatus.DELIVERED != batch.status() && BatchStatus.COMPLETED != batch.status()) {
      var message = String.format("Marking Batch with batchId: %s as %s failed. "
              + "%s status is invalid, %s or %s expected", batchId, BatchStatus.DELIVERED.name(),
          batch.status(), BatchStatus.DELIVERED.name(), BatchStatus.COMPLETED.name());
      log.error(message);
      throw new IllegalStateException(message);
    }
  }

  private Batch findBatch(String batchId) {
    return batchRepository.findById(batchId)
        .orElseThrow(() -> {
          var message = String.format("No batch found for batch id: %s", batchId);
          log.error(message);
          return new NoSuchElementException(message);
        });
  }

  private Batch logIfAlreadyExists(Batch batch) {
    log.info("Batch for the given id: {} already exists.", batch.id());
    return batch;
  }

  private Optional<Batch> registerNew(RegisterBatchCommand registerBatchCommand) {
    log.info("Registering new batch with id: {}", registerBatchCommand.id());

    var defaultModel = defaultModelService.getForSolving();
    var analysis = analysisService.create(defaultModel);
    var batch = Batch.builder()
        .id(registerBatchCommand.id())
        .analysisName(analysis.name())
        .policyName(defaultModel.policyName())
        .alertsCount(registerBatchCommand.alertCount())
        .batchMetadata(registerBatchCommand.batchMetadata())
        .status(BatchStatus.STORED)
        .build();
    return Optional.of(batchRepository.create(batch));
  }

  private void markBatchAsError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    log.info("Set batch status to ERROR with id: {} and set error description: {}",
        notifyBatchErrorCommand.id(), notifyBatchErrorCommand.errorDescription());

    batchRepository.updateStatusAndErrorDescription(
        notifyBatchErrorCommand.id(),
        BatchStatus.ERROR,
        notifyBatchErrorCommand.errorDescription());
  }

  private void registerNewAsError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    log.info("Registering new batch as ERROR with id: {} and error description: {}",
        notifyBatchErrorCommand.id(), notifyBatchErrorCommand.errorDescription());

    batchRepository.create(Batch.error(
        notifyBatchErrorCommand.id(),
        notifyBatchErrorCommand.errorDescription(),
        notifyBatchErrorCommand.batchMetadata()));

    log.info("New batch registered as error with id: {}", notifyBatchErrorCommand.id());
  }

  private void markBatchAsCompleted(String batchId) {
    log.info("Set batch status to COMPLETED with batch id: {}", batchId);
    batchRepository.updateStatusToCompleted(batchId);
  }

  private void publishBatchCompleted(Batch batch, List<String> alertNames) {
    var batchCompletedStatistics =
        batchStatisticsService.createBatchCompletedStatistics(batch.id(), batch.analysisName());

    eventPublisher.publish(
        BatchCompleted.builder()
            .id(batch.id())
            .analysisId(batch.analysisName())
            .batchMetadata(batch.batchMetadata())
            .alertIds(alertNames)
            .statistics(batchCompletedStatistics)
            .build()
    );
  }
}
