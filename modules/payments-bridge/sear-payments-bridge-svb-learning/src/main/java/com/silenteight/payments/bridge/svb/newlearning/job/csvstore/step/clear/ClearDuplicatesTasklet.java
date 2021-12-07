package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.clear;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class ClearDuplicatesTasklet implements Tasklet {

  private final LearningDataAccess learningDataAccess;

  @Transactional
  @Override
  public RepeatStatus execute(
      StepContribution contribution,
      ChunkContext chunkContext) {
    learningDataAccess.removeDuplicates();
    return RepeatStatus.FINISHED;
  }

}
