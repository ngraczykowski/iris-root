/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.infrastructure.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomUtils;

@UtilityClass
@Slf4j
public class MockUtils {

  public void randomSleep(int minMs, int maxMs) {
    try {
      var timeToSleep = RandomUtils.nextInt(minMs, maxMs);
      log.info("Sleeping for: {} ms...", timeToSleep);
      Thread.sleep(timeToSleep);
      log.info("Done sleeping");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Thread interrupted while sleeping in Mock", e);
    }
  }

}
