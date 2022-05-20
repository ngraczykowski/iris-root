package com.silenteight.payments.bridge.data.retention.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DataRetentionConstants {

  // Jobs
  public static final String SEND_ALERTS_EXPIRED_JOB_NAME = "send-alerts-expired-job";
  public static final String SEND_REMOVE_PII_JOB_NAME = "send-remove-pii-job";

  // Steps
  public static final String SEND_ALERTS_EXPIRED_STEP_NAME = "send-alerts-expired-step";
  public static final String SEND_REMOVE_PII_STEP_NAME = "send-remove-pii-step";
}
