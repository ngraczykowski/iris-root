package com.silenteight.payments.bridge.app.batch;

import org.springframework.batch.core.JobParameters;

public interface JobMaintainer {

  void restartUncompletedJobs();

  void runJobByName(String name, JobParameters parameters);
}
