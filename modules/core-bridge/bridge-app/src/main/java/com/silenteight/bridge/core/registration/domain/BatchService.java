package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.model.BatchId;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class BatchService {

  private final EventPublisher eventPublisher;
  private final AnalysisService analysisService;
  private final BatchRepository batchRepository;
  private final DefaultModelService defaultModelService;

  BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchRepository.findById(registerBatchCommand.id())
        .map(this::logIfAlreadyExists)
        .or(() -> registerNew(registerBatchCommand))
        .map(BatchId::from)
        .orElseThrow();
  }

  BatchId findBatch(String analysisName) {
    return batchRepository.findByName(analysisName)
        .map(BatchId::from)
        .orElseThrow();
  }

  void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchRepository.findById(notifyBatchErrorCommand.id())
        .ifPresentOrElse(
            batch -> markBatchAsError(notifyBatchErrorCommand),
            () -> registerNewAsError(notifyBatchErrorCommand));
    eventPublisher.publish(
        new BatchError(
            notifyBatchErrorCommand.id(),
            notifyBatchErrorCommand.errorDescription(),
            notifyBatchErrorCommand.batchMetadata())
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

  private Batch logIfAlreadyExists(Batch batch) {
    log.info("Batch for the given id: {} already exists.", batch.id());
    return batch;
  }

  private Optional<Batch> registerNew(RegisterBatchCommand registerBatchCommand) {
    log.info("Registering new batch with id: {}", registerBatchCommand.id());

    var defaultModel = defaultModelService.getForSolving();
    var analysis = analysisService.create(defaultModel);
    var batch = Batch.newOne(
        registerBatchCommand.id(),
        analysis.name(),
        registerBatchCommand.alertCount(),
        registerBatchCommand.batchMetadata());
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
    eventPublisher.publish(
        new BatchCompleted(
            batch.id(),
            batch.analysisName(),
            alertNames,
            batch.batchMetadata())
    );
  }
}
