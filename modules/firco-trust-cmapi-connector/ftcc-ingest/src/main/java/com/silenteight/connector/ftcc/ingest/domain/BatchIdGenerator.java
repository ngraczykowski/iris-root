package com.silenteight.connector.ftcc.ingest.domain;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class BatchIdGenerator {

  public UUID generate() {
    return randomUUID();
  }
}
