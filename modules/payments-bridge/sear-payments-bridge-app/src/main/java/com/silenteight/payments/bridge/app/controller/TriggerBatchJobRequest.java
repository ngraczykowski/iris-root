package com.silenteight.payments.bridge.app.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.List;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.learning.step.reetl.JobParameterTransformer.PREFIX_FOR_JOB_PARAMETERS_FEATURE_INPUT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class TriggerBatchJobRequest {

  String jobName;

  String fileName;

  List<String> features;

  public JobParameters toJobParameters() {
    final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, fileName);

    if (features != null && !features.isEmpty()) {
      var jobParameters = new JobParametersBuilder();
      for (String feature : features) {
        jobParameters.addString(PREFIX_FOR_JOB_PARAMETERS_FEATURE_INPUT + feature, feature);
      }
      jobParametersBuilder.addJobParameters(jobParameters.toJobParameters());
    }

    return jobParametersBuilder
        .toJobParameters();
  }
}
