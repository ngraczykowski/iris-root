/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.scb-bridge.traffic-manager.semaphore")
record GnsRtSemaphoreProperties(boolean enabled,
                                Duration timer) {

  public long getTimerSeconds() {
    return timer.toSeconds();
  }
}
