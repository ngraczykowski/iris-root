package com.silenteight.payments.bridge.app.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import java.util.Optional;

public interface JobMaintainer {

  void restartUncompletedJobs();

  Optional<JobExecution> runJobByName(String name, JobParameters parameters);
}
