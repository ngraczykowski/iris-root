package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.SolvingBatchCompleted;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class BatchCompletionSolvingStrategy implements BatchCompletionStrategy {

  private final BatchEventPublisher eventPublisher;
  private final BatchRepository batchRepository;

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SOLVING;
  }

  @Override
  public void completeBatch(Batch batch) {
    markSolvingBatchAsCompleted(batch.id());
    publishSolvingBatchCompleted(batch);
  }

  private void markSolvingBatchAsCompleted(String batchId) {
    log.info(
        "Set solving batch status to [{}] with batch id [{}].", BatchStatus.COMPLETED, batchId);
    batchRepository.updateStatusToCompleted(batchId);
  }

  private void publishSolvingBatchCompleted(Batch batch) {
    eventPublisher.publish(SolvingBatchCompleted.builder()
        .id(batch.id())
        .analysisName(batch.analysisName())
        .batchMetadata(batch.batchMetadata())
        .priority(batch.batchPriority())
        .build()
    );
  }
}
