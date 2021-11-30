package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Service;

@Service("storeCsvFileStepListener")
@RequiredArgsConstructor
@Slf4j
class StoreCsvFileStepListener implements StepExecutionListener {

  private final LearningDataAccess learningDataAccess;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    var fileId = stepExecution.getJobParameters().getLong("fileId");
    learningDataAccess.updateFileStatus(fileId, CsvProcessingFileStatus.PROCESSING);
    log.info("Before step execution {} file {} PROCESSING", stepExecution.getStepName(), fileId);
  }

  @Override
  public ExitStatus afterStep(
      StepExecution stepExecution) {
    var fileId = stepExecution.getJobParameters().getLong("fileId");
    learningDataAccess.updateFileStatus(fileId, CsvProcessingFileStatus.STORED);
    log.info("After step execution {} file:{} STORED", stepExecution.getStepName(), fileId);
    return null;
  }
}
