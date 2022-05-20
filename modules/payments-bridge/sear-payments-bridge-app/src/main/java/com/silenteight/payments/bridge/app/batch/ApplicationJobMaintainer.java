package com.silenteight.payments.bridge.app.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.StopBatchJobEvent;
import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
class ApplicationJobMaintainer implements JobMaintainer {

  private final List<Job> jobs;

  private final JobLauncher searJobLauncher;
  private final JobExplorer jobExplorer;
  private final JobOperator jobOperator;
  private final JobRegistry jobRegistry;
  private final JobRepository jobRepository;

  @PostConstruct
  void init() {
    log.info("Initialize job maintainer");
    createJobRegistry();
  }

  @EventListener
  public void triggerBatchProcessingListener(TriggerBatchJobEvent triggerBatchJobEvent) {
    runJobByName(triggerBatchJobEvent.getJobName(), triggerBatchJobEvent.getParameters());
  }

  @EventListener
  public void stopBatchJobListener(StopBatchJobEvent stopBatchJobEvent) {
    stopJobByJobId(stopBatchJobEvent.getJobExecutionId());
  }

  @Override
  public void restartUncompletedJobs() {
    log.info("Restarting all available jobs");
    jobs.forEach(job -> checkAndRestartLastJobExecutions(job.getName()));
  }

  @Override
  public Optional<JobExecution> runJobByName(String jobName, JobParameters parameters) {

    var registeredJob =
        jobs.stream()
            .filter(job -> jobName.equals(job.getName()))
            .findFirst();
    if (registeredJob.isEmpty()) {
      throw new JobNameNotFoundException(String.format("Could not find %s job name", jobName));
    }

    return runJob(registeredJob.get(), createJobParametersWithTime(parameters));
  }

  private void stopJobByJobId(Long jobId) {
    try {
      jobOperator.stop(jobId);
    } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
      log.warn("Couldn't abandon job {} cause {}", jobId, e.getMessage());
    }
  }

  private static JobParameters createJobParametersWithTime(JobParameters parameters) {
    return new JobParametersBuilder()
        .addDate("time", Calendar.getInstance().getTime())
        .addJobParameters(parameters)
        .toJobParameters();
  }

  // Required to be able to restart jobs which are searched by restart action inside registry.
  private void createJobRegistry() {
    jobs.forEach(job -> {
      try {
        log.info("Registering job:{}", job.getName());
        jobRegistry.register(new ReferenceJobFactory(job));
      } catch (DuplicateJobException e) {
        log.warn("Registration failure duplicated job:{}", job.getName());
      }
    });
  }

  private void checkAndRestartLastJobExecutions(String jobName) {
    var jobInstanceSize = getJobInstanceCount(jobName);
    var jobInstances =
        jobExplorer.getJobInstances(jobName, 0, jobInstanceSize);
    jobInstances.forEach(jobInstance -> {
      var jobExecution = jobExplorer.getLastJobExecution(jobInstance);
      if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
        restartJobExecution(jobExecution);
      } else {
        log.trace("Job:{} execution status was:{} with exitStatus: {} restart ignored",
            jobName, jobExecution.getStatus(), jobExecution.getExitStatus());
      }
    });
  }

  private int getJobInstanceCount(String jobName) {
    try {
      return jobExplorer.getJobInstanceCount(jobName);
    } catch (NoSuchJobException e) {
      log.warn("Request jobSize for jobName:{} could not be retrieved, no such job", jobName);
    }
    return 0;
  }

  private void restartJobExecution(JobExecution lastJobExecution) {
    log.info(
        "Restarting jobId:{} execution status was:{} parameters: {} ",
        lastJobExecution.getJobId(),
        lastJobExecution.getStatus(),
        lastJobExecution.getJobParameters());
    lastJobExecution.setStatus(BatchStatus.STOPPED);
    lastJobExecution.setEndTime(new Date());
    jobRepository.update(lastJobExecution);
    log.info("Last job:{} execution:{} marked as STOPPED", lastJobExecution.getJobId(),
        lastJobExecution.getId());
    try {
      var restartId = jobOperator.restart(lastJobExecution.getId());
      log.info(
          "Job {} restarted with assigned executionId:{}", lastJobExecution.getJobId(), restartId);
    } catch (JobInstanceAlreadyCompleteException |
        NoSuchJobExecutionException |
        NoSuchJobException |
        JobRestartException |
        JobParametersInvalidException e) {
      log.warn("Job restart mechanism failed due to:", e);
    }
  }

  private Optional<JobExecution> runJob(Job job, JobParameters params) {
    try {
      return Optional.of(searJobLauncher.run(job, params));
    } catch (JobExecutionAlreadyRunningException e) {
      log.warn("Job execution failure already running {}", job.getName());
    } catch (JobRestartException e) {
      log.warn("Job restart failure", e);
    } catch (JobInstanceAlreadyCompleteException e) {
      log.warn("Job {} already completed with params:{}", job.getName(), params);
    } catch (JobParametersInvalidException e) {
      log.warn("Job parameters exception", e);
    }
    return Optional.empty();
  }
}

