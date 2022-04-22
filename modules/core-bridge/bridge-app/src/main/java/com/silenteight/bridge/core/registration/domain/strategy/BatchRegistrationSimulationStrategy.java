package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class BatchRegistrationSimulationStrategy implements BatchRegistrationStrategy {

  private final BatchRepository batchRepository;

  @Override
  public Batch register(RegisterBatchCommand registerBatchCommand) {
    log.info("Registering new simulation batch with id: {}", registerBatchCommand.id());

    var batch = Batch.builder()
        .id(registerBatchCommand.id())
        .alertsCount(registerBatchCommand.alertCount())
        .batchMetadata(registerBatchCommand.batchMetadata())
        .status(BatchStatus.STORED)
        .batchPriority(registerBatchCommand.batchPriority())
        .isSimulation(true)
        .build();

    return batchRepository.create(batch);
  }

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SIMULATION;
  }
}
