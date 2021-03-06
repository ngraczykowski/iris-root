package com.silenteight.payments.bridge.svb.learning.step.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.DeleteFileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.BUCKET_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;

@RequiredArgsConstructor
@Slf4j
public class DeleteFileTasklet implements Tasklet {

  private final CsvFileResourceProvider resourceProvider;
  private final boolean skipDeletion;

  @Override
  public RepeatStatus execute(
      StepContribution contribution,
      ChunkContext chunkContext) {
    if (skipDeletion) {
      log.info("File deletion tasklet will be skipped due to configuration setup.");
      return RepeatStatus.FINISHED;
    } else {
      var fileName =
          contribution.getStepExecution().getJobParameters().getString(FILE_NAME_PARAMETER);
      var bucketName =
          contribution.getStepExecution().getJobParameters().getString(BUCKET_NAME_PARAMETER);
      log.info("Tasklet of deleting s3 file has been executed:{}", fileName);
      resourceProvider.deleteFile(DeleteFileRequest.builder()
          .bucket(bucketName)
          .object(fileName)
          .build());
      return RepeatStatus.FINISHED;
    }
  }

}
