package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;

@DisallowConcurrentExecution
@NoArgsConstructor
@Setter
@Slf4j
class QuartzBatchJobLauncher implements InterruptableJob {

  private String jobName;
  private JobLauncher jobLauncher;
  private JobLocator jobLocator;
  private JobOperator jobOperator;

  @Override
  public void execute(JobExecutionContext context) {
    try {
      runJob(context.getJobDetail().getJobDataMap());
      stopAndAbandonOtherExecutions();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void runJob(JobDataMap jobDataMap) throws JobExecutionException {
    Job job = jobLocator.getJob(jobName);

    JobParameters params = new JobParametersBuilder()
        .addString("JobID", String.valueOf(System.currentTimeMillis()))
        .addString(GnsSyncConstants.GNS_SYNC_MODE_KEY, jobDataMap.getString(
            GnsSyncConstants.GNS_SYNC_MODE_KEY))
        .toJobParameters();

    jobLauncher.run(job, params);
  }

  private void stopAndAbandonOtherExecutions() throws NoSuchJobException {
    jobOperator.getRunningExecutions(jobName).forEach(jobExecutionId -> {
      stopJobExecution(jobExecutionId);
      abandonJobExecution(jobExecutionId);
    });
  }

  private void stopJobExecution(Long jobExecutionId) {
    try {
      jobOperator.stop(jobExecutionId);
    } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
      log.warn("Failed to stop job execution: jobExecutionId={}", jobExecutionId, e);
    }
  }

  private void abandonJobExecution(Long jobExecutionId) {
    try {
      jobOperator.abandon(jobExecutionId);
    } catch (NoSuchJobExecutionException | JobExecutionAlreadyRunningException e) {
      log.warn("Failed to abandon job execution: jobExecutionId={}", jobExecutionId, e);
    }
  }

  @Override
  public void interrupt() {
    // FIXME(ahaczewski): We should be able to interrupt running job by interrupting thread
    //  that is executing Step - Spring Batch has checks in place that will stop progressing
    //  after a chunk is completed. But right now stopping Sync fails to bring execution to this
    //  point, therefore I'm leaving only log here in hope to have a solution sometime in the
    //  future.
    log.warn("INTERRUPTED");
  }
}
