package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.domain.GnsSyncService;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import static com.silenteight.customerbridge.common.batch.BatchUtils.getJobContext;
import static com.silenteight.customerbridge.common.batch.BatchUtils.getStepExecutionContext;
import static com.silenteight.customerbridge.common.domain.GnsSyncConstants.GNS_SYNC_ID_KEY;
import static com.silenteight.customerbridge.common.domain.GnsSyncConstants.GNS_SYNC_MODE_KEY;

@RequiredArgsConstructor
class FinishGnsSyncTasklet implements Tasklet {

  private final GnsSyncService syncService;

  @Override
  public RepeatStatus execute(
      @NotNull StepContribution stepContribution,
      @NotNull ChunkContext chunkContext) {

    ExecutionContext jobContext = getJobContext(chunkContext);
    int syncId = jobContext.getInt(GNS_SYNC_ID_KEY);
    syncService.finishSync(syncId);

    tryToAbandonPreviousSyncIfNeeded(chunkContext);

    return RepeatStatus.FINISHED;
  }

  private void tryToAbandonPreviousSyncIfNeeded(@NotNull ChunkContext chunkContext) {
    ExecutionContext stepExecutionContext = getStepExecutionContext(chunkContext);
    if (stepExecutionContext.containsKey(GNS_SYNC_MODE_KEY)) {
      String syncMode = stepExecutionContext.getString(GNS_SYNC_MODE_KEY);

      if (syncService.isRunningSync(syncMode))
        syncService.abandonSync(syncMode);
    }
  }
}
