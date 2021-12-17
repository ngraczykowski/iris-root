package com.silenteight.bridge.core.registration.domain;

public record BatchId(String id) {

  static BatchId from(Batch batch) {
    return new BatchId(batch.id());
  }
}
