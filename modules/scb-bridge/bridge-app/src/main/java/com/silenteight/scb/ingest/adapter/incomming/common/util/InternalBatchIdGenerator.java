package com.silenteight.scb.ingest.adapter.incomming.common.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class InternalBatchIdGenerator {

  public String generate() {
    return UUID.randomUUID().toString();
  }
}
