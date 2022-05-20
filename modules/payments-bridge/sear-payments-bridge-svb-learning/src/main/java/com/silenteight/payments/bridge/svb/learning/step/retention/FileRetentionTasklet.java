package com.silenteight.payments.bridge.svb.learning.step.retention;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateFileRetentionUseCase;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;

@RequiredArgsConstructor
class FileRetentionTasklet implements Tasklet {

  private final CreateFileRetentionUseCase createFileRetentionUseCase;

  @Override
  public RepeatStatus execute(
      StepContribution contribution,
      @Nonnull ChunkContext chunkContext) {

    var fileName =
        contribution.getStepExecution().getJobParameters().getString(FILE_NAME_PARAMETER);

    createFileRetentionUseCase.create(
        List.of(FileDataRetention.builder().fileName(fileName).build()));

    return RepeatStatus.FINISHED;
  }
}
