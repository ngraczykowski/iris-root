package com.silenteight.payments.bridge.svb.newlearning.step.remove;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveDuplicatesTasklet implements Tasklet {

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
