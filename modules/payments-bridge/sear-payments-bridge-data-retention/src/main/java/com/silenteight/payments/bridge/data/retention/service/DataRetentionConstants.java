package com.silenteight.payments.bridge.data.retention.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DataRetentionConstants {

  // Jobs
  public static final String SEND_ALERTS_EXPIRED_JOB_NAME = "send-alerts-expired-job";

  // Steps
  public static final String SEND_ALERTS_EXPIRED_STEP_NAME = "send-alerts-expired-step";
}
