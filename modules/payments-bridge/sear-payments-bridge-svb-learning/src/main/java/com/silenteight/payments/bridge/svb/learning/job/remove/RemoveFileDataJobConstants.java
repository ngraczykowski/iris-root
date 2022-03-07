package com.silenteight.payments.bridge.svb.learning.job.remove;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RemoveFileDataJobConstants {

  // Jobs
  public static final String REMOVE_FILE_DATA_JOB_NAME = "remove-file-data-job";

  // Steps
  public static final String REMOVE_CSV_ROW_STEP_NAME = "remove-csv-row-step";
  public static final String REMOVE_ALERT_STEP_NAME = "remove-alert-step";
  public static final String REMOVE_HIT_STEP_NAME = "remove-hit-step";
  public static final String REMOVE_ACTION_STEP_NAME = "remove-action-step";
}
