/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class InternalBatchIdGenerator {

  public String generate() {
    return UUID.randomUUID().toString();
  }
}
