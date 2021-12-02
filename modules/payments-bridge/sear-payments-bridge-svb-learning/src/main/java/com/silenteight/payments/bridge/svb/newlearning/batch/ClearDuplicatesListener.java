package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ClearDuplicatesListener implements JobExecutionListener {

  private final LearningDataAccess learningDataAccess;

  @Override
  public void beforeJob(JobExecution jobExecution) {

  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    learningDataAccess.removeDuplicates();
  }
}
