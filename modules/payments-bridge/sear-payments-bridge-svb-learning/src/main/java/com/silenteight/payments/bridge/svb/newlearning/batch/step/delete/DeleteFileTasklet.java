package com.silenteight.payments.bridge.svb.newlearning.batch.step.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobParameters.FILE_ID_PARAMETER;


@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteFileTasklet implements Tasklet {

  private final CsvFileResourceProvider resourceProvider;
  private final LearningDataAccess learningDataAccess;

  @Override
  public RepeatStatus execute(
      StepContribution contribution,
      ChunkContext chunkContext) {

    var fileId =
        contribution.getStepExecution().getJobParameters().getLong(FILE_ID_PARAMETER);
    log.info("Tasklet of deleting s3 file has been executed:{}", fileId);
    var learningFile = learningDataAccess.findLearningFileById(fileId);

    learningFile.ifPresent(file ->
        resourceProvider.deleteLearningFile(DeleteLearningFileRequest.builder()
            .bucket(file.getBucketName())
            .object(file.getFileName())
            .build())
    );

    return RepeatStatus.FINISHED;
  }

}
