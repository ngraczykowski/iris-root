package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class LearningCsvRowWriter<T> extends JpaItemWriter<LearningCsvRowEntity> {

  private final LearningCsvRowRepository repository;
  private final Long learningFileId;
  private final Long jobId;

  @Override
  public void write(List<? extends LearningCsvRowEntity> items) {
    items.forEach(item -> {
      if (log.isDebugEnabled()) {
        log.debug("Storing item id:{} systemId:{}", item.getFkcoId(), item.getFkcoVSystemId());
      }
      item.setJobId(jobId);
      item.setLearningfile(learningFileId);
      repository.save(item);
    });
    log.info("Stored items chunkSize:{}", items.size());
  }
}
