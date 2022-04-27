package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.SimulationBatchCompleted;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class BatchCompletionSimulationStrategy implements BatchCompletionStrategy {

  private final BatchEventPublisher eventPublisher;
  private final BatchRepository batchRepository;

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SIMULATION;
  }

  @Override
  public void completeBatch(Batch batch) {
    markBatchAsCompleted(batch.id());
    publishBatchCompleted(batch);
  }

  private void markBatchAsCompleted(String batchId) {
    log.info("Set simulation batch status to [{}] with id [{}].", BatchStatus.COMPLETED, batchId);
    batchRepository.updateStatusToCompleted(batchId);
  }

  private void publishBatchCompleted(Batch batch) {
    eventPublisher.publish(SimulationBatchCompleted.builder()
        .id(batch.id())
        .analysisName(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .build()
    );
  }
}
