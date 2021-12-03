package com.silenteight.payments.bridge.app.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
class ApplicationJobMaintainer implements JobMaintainer {

  private final List<Job> jobs;

  private final JobLauncher jobLauncher;
  private final JobExplorer jobExplorer;
  private final JobOperator jobOperator;
  private final JobRegistry jobRegistry;
  private final JobRepository jobRepository;

  @PostConstruct
  void init() {
    log.info("Initialize job maintainer");
    createJobRegistry();
  }

  @Override
  public void restartUncompletedJobs() {
    log.info("Restarting all available jobs");
    jobs.forEach(job -> checkAndRestartLastJobExecution(job.getName()));
  }

  @Override
  public void runJobByName(String jobName, JobParameters parameters) {
    var registeredJob =
        jobs.stream()
            .filter(job -> jobName.equals(job.getName()))
            .findFirst();
    if (registeredJob.isPresent()) {
      runJob(registeredJob.get(), parameters);
    } else {
      log.warn("Job is not registered:{}", jobName);
    }
  }

  // Required to be able to restart jobs which are searched by restart action inside registry.
  private void createJobRegistry() {
    jobs.stream().forEach(job -> {
      try {
        log.info("Registering job:{}", job.getName());
        jobRegistry.register(new ReferenceJobFactory(job));
      } catch (DuplicateJobException e) {
        log.warn("Registration failure duplicated job:{}", job.getName());
      }
    });
  }

  private void checkAndRestartLastJobExecution(String jobName) {
    var lastJobInstance = jobExplorer.getLastJobInstance(jobName);
    if (lastJobInstance != null) {
      var lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);
      if (BatchStatus.FAILED.equals(lastJobExecution.getStatus())) {
        restartJobExecution(lastJobExecution);
      } else {
        log.info("Last job {} execution status was:{} restart ignored", jobName,
            lastJobExecution.getStatus());
      }
    } else {
      log.info(
          "There is no job instance registered for jobName:{} ignore restarting execution",
          jobName);
    }
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
    log.info("Job:{} last execution marked as STOPPED", lastJobExecution.getJobId());
    try {
      var restartId = jobOperator.restart(lastJobExecution.getId());
      log.info(
          "Job restarted:{} with assigned executionId:{}", lastJobExecution.getJobId(), restartId);
    } catch (JobInstanceAlreadyCompleteException |
        NoSuchJobExecutionException |
        NoSuchJobException |
        JobRestartException |
        JobParametersInvalidException e) {
      log.warn("Job restart mechanism failed due to:", e);
    }
  }

  private void runJob(Job job, JobParameters params) {
    try {
      jobLauncher.run(job, params);
    } catch (JobExecutionAlreadyRunningException e) {
      log.warn("Job execution failure already running {}", job.getName());
    } catch (JobRestartException e) {
      log.warn("Job restart failure", e);
    } catch (JobInstanceAlreadyCompleteException e) {
      log.warn("Job already completed with params:{}", job.getName(), params);
    } catch (JobParametersInvalidException e) {
      log.warn("Job parameters exception", e);
    }
  }
}

