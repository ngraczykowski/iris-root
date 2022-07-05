/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.firco.alertmessage.service;

import java.time.Duration;

public interface AlertMessageConfiguration {
  long getStoredQueueLimit();

  Duration getDecisionRequestedTime();

  boolean isOriginalMessageDeletedAfterRecommendation();

  int getMaxHitsPerAlert();
}
