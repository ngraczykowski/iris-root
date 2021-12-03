package com.silenteight.payments.bridge.svb.newlearning.batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.BUCKET_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.FILE_NAME_PARAMETER;

public class StoreStepFixture {

  public static JobParameters toParams(String fileName, String bucketName) {
    return new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, fileName)
        .addString(BUCKET_NAME_PARAMETER, bucketName)
        .toJobParameters();
  }
}
