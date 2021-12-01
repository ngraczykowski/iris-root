package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
@Slf4j
class StoreCsvFileStepProcessor implements
    ItemProcessor<LearningCsvRowEntity, LearningCsvRowEntity> {

  private final Long learningFileId;
  private final Long jobId;

  @Override
  public LearningCsvRowEntity process(LearningCsvRowEntity item) {
    if (log.isDebugEnabled()) {
      log.debug("Processing item id:{} systemId:{}", item.getFkcoId(), item.getFkcoVSystemId());
    }
    item.setLearningfile(learningFileId);
    item.setJobId(jobId);
    return item;
  }
}
