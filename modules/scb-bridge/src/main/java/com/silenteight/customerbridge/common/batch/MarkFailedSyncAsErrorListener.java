package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.domain.GnsSyncService;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.customerbridge.common.domain.GnsSyncConstants.GNS_SYNC_ID_KEY;

@RequiredArgsConstructor
@Slf4j
class MarkFailedSyncAsErrorListener extends JobExecutionListenerSupport {

  private final GnsSyncService gnsSyncService;

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (!BatchUtils.isJobFailed(jobExecution))
      return;

    ExecutionContext jobContext = jobExecution.getExecutionContext();
    if (!jobContext.containsKey(GNS_SYNC_ID_KEY))
      return;

    int syncId = jobContext.getInt(GNS_SYNC_ID_KEY);
    finishSync(syncId, jobExecution.getAllFailureExceptions());
    jobExecution.setStatus(BatchStatus.COMPLETED);
  }

  private void finishSync(long syncId, List<Throwable> failureExceptions) {
    String errorMessage = getErrorMessage(failureExceptions);
    log.error("Finishing gnsparty synchronization [id: {}] with error: {}", syncId, errorMessage);
    gnsSyncService.finishSyncWithError(syncId, errorMessage);
  }

  private static String getErrorMessage(List<Throwable> failureExceptions) {
    return failureExceptions
          .stream()
          .map(Throwable::getMessage)
          .collect(Collectors.joining(", "));
  }
}
