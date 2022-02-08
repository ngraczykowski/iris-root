package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LearningJobConstants {

  // Jobs
  public static final String STORE_CSV_JOB_NAME = "store-csv-job";
  public static final String HISTORICAL_RISK_ASSESSMENT_JOB_NAME = "historical-risk-assessment-job";
  public static final String CONTEXTUAL_LEARNING_JOB_NAME = "contextual-learning-job";

  // Step
  public static final String STORE_FILE_STEP = "store-csv-file";
  public static final String TRANSFORM_ACTION_STEP = "transform-action";
  public static final String STEP_TRANSFORM_ALERT = "transform-alert";
  public static final String STEP_TRANSFORM_HIT = "transform-hit";
  public static final String HISTORICAL_ASSESSMENT_RESERVATION_STEP =
      "historical-assessment-reservation";
  public static final String HISTORICAL_ASSESSMENT_STORE_STEP =
      "historical-assessment-store";
  public static final String CONTEXTUAL_LEARNING_RESERVATION_STEP =
      "contextual-learning-reservation";
  public static final String CONTEXTUAL_LEARNING_STORE_STEP =
      "contextual-learning-store";
  public static final String REMOVE_DUPLICATES_STEP = "remove-duplicates-step";

  // Params
  public static final String FILE_NAME_PARAMETER = "file-name";
  public static final String BUCKET_NAME_PARAMETER = "bucket-name";

}
