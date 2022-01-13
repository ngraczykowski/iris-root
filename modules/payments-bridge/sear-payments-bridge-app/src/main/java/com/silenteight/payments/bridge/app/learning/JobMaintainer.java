package com.silenteight.payments.bridge.app.learning;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import java.util.Optional;

public interface JobMaintainer {

  void restartUncompletedJobs();

  Optional<JobExecution> runJobByName(String name, JobParameters parameters);
}
