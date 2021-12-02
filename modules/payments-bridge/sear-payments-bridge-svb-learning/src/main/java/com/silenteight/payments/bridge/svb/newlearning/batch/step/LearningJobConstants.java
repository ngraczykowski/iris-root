package com.silenteight.payments.bridge.svb.newlearning.batch.step;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LearningJobConstants {

  public static final String STORE_FILE_STEP = "store-csv-file";

  public static final String TRANSFORM_ACTION_STEP = "transform-action";

  public static final String STEP_TRANSFORM_ALERT = "transform-alert";

  public static final String STEP_TRANSFORM_HIT = "transform-hit";

  public static final String STEP_TRANSFORM_RECORD = "transform-listed-record";

  public static final String STEP_TRANSFORM_ALERTED_MESSAGE = "transform-alerted-message";

  public static final String STEP_TRANSFORM_STATUS = "transform-status";
}
