package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand;
import com.silenteight.bridge.core.registration.domain.model.Analysis;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.DefaultModel;
import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.VerifyBatchTimeoutPublisher;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class BatchSolvingRegistrationStrategy implements BatchRegistrationStrategy {

  private final AnalysisService analysisService;
  private final DefaultModelService defaultModelService;
  private final BatchRepository batchRepository;
  private final VerifyBatchTimeoutPublisher verifyBatchTimeoutPublisher;

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SOLVING;
  }

  @Override
  public Batch register(RegisterBatchCommand registerBatchCommand) {
    log.info("Registering new solving batch with id: {}", registerBatchCommand.id());

    var defaultModel = defaultModelService.getForSolving();
    var analysis = analysisService.create(defaultModel);
    var createdBatch = createBatch(registerBatchCommand, defaultModel, analysis);
    verifyBatchTimeoutPublisher.publish(new VerifyBatchTimeoutEvent(createdBatch.id()));

    return createdBatch;
  }

  private Batch createBatch(
      RegisterBatchCommand registerBatchCommand, DefaultModel defaultModel, Analysis analysis) {
    return batchRepository.create(Batch.builder()
        .id(registerBatchCommand.id())
        .analysisName(analysis.name())
        .policyName(defaultModel.policyName())
        .alertsCount(registerBatchCommand.alertCount())
        .batchMetadata(registerBatchCommand.batchMetadata())
        .status(BatchStatus.STORED)
        .batchPriority(registerBatchCommand.batchPriority())
        .isSimulation(false)
        .build());
  }
}
