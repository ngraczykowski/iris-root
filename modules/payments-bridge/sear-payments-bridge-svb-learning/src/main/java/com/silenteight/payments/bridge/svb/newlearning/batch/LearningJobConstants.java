package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LearningJobConstants {

  // Jobs
  public static final String LEARNING_JOB_NAME = "learning-job";

  // Step
  public static final String STORE_FILE_STEP = "store-csv-file";
  public static final String TRANSFORM_ACTION_STEP = "transform-action";
  public static final String STEP_TRANSFORM_ALERT = "transform-alert";
  public static final String STEP_TRANSFORM_HIT = "transform-hit";
  public static final String STEP_TRANSFORM_RECORD = "transform-listed-record";
  public static final String REMOVE_DUPLICATES_STEP = "remove-duplicates";

  // Params
  public static final String FILE_NAME_PARAMETER = "file-name";
  public static final String BUCKET_NAME_PARAMETER = "bucket-name";

}
