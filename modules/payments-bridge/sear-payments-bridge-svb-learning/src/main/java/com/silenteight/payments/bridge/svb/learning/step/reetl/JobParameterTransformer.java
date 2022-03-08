package com.silenteight.payments.bridge.svb.learning.step.reetl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobParameterTransformer {

  public static final String PREFIX_FOR_JOB_PARAMETERS_FEATURE_INPUT = "feature-";

  static List<String> determineFeatureInputList(
      final JobParameters jobParameters) {

    final Map<String, JobParameter> parameters = jobParameters.getParameters();

    final List<String> featureInputs = new ArrayList<>();

    for (Entry<String, JobParameter> entry : parameters.entrySet()) {
      if (entry.getKey().contains(PREFIX_FOR_JOB_PARAMETERS_FEATURE_INPUT)) {
        featureInputs.add(((String) entry.getValue().getValue()));
      }
    }

    return featureInputs;
  }
}
