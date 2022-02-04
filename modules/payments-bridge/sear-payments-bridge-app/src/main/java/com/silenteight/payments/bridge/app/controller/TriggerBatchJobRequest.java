package com.silenteight.payments.bridge.app.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class TriggerBatchJobRequest {

  String jobName;

  String fileName;

  public JobParameters toJobParameters() {
    return new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, fileName)
        .toJobParameters();
  }
}
