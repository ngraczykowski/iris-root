package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.domain.GnsSyncService;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import javax.annotation.Nonnull;

import static com.silenteight.customerbridge.common.batch.BatchUtils.getJobContext;
import static com.silenteight.customerbridge.common.batch.BatchUtils.getStepExecutionContext;
import static com.silenteight.customerbridge.common.domain.GnsSyncConstants.GNS_SYNC_ID_KEY;
import static com.silenteight.customerbridge.common.domain.GnsSyncConstants.GNS_SYNC_MODE_KEY;

@Slf4j
@RequiredArgsConstructor
class CreateNewGnsSyncTasklet implements Tasklet {

  private final GnsSyncService syncService;

  @Override
  public RepeatStatus execute(
      @NotNull StepContribution stepContribution,
      @NotNull ChunkContext chunkContext) {

    ExecutionContext jobContext = getJobContext(chunkContext);
    ExecutionContext stepExecutionContext = getStepExecutionContext(chunkContext);

    String syncMode = getAndValidateSyncMode(stepExecutionContext);
    Long syncId = syncService.startNewSync(syncMode);
    log.info("Assigned gnsSyncId: {}", syncId);
    jobContext.putInt(GNS_SYNC_ID_KEY, Math.toIntExact(syncId));

    return RepeatStatus.FINISHED;
  }

  @Nonnull
  private static String getAndValidateSyncMode(ExecutionContext stepExecutionContext) {
    if (!stepExecutionContext.containsKey(GNS_SYNC_MODE_KEY))
      throw new IllegalStateException("Gns Sync Mode has not been set");

    return stepExecutionContext.getString(GNS_SYNC_MODE_KEY);
  }
}
