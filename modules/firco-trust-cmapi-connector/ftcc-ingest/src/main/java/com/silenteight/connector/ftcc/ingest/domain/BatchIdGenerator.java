package com.silenteight.connector.ftcc.ingest.domain;

import java.util.UUID;

import static java.util.UUID.randomUUID;

class BatchIdGenerator {

  UUID generate() {
    return randomUUID();
  }
}
