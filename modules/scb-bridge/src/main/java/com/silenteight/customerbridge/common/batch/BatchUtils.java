package com.silenteight.customerbridge.common.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

import javax.annotation.Nonnull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class BatchUtils {

  static ExecutionContext getJobContext(ChunkContext chunkContext) {
    return getStepExecution(chunkContext).getJobExecution().getExecutionContext();
  }

  static ExecutionContext getStepExecutionContext(ChunkContext chunkContext) {
    return getStepExecution(chunkContext).getExecutionContext();
  }

  static boolean isJobFailed(JobExecution jobExecution) {
    return jobExecution.getStatus() == BatchStatus.FAILED
        || jobExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode());
  }

  @Nonnull
  private static StepExecution getStepExecution(ChunkContext chunkContext) {
    return chunkContext.getStepContext().getStepExecution();
  }
}
