/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest;

import lombok.Getter;

import org.springframework.stereotype.Component;

@Component
class IngestedLearningAlertsCounter {

  @Getter
  private long count;

  void increment(int alertsCount) {
    count += alertsCount;
  }
}
