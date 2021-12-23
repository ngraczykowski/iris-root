package com.silenteight.bridge.core.registration.domain.model;

public record BatchId(String id) {

  public static BatchId from(Batch batch) {
    return new BatchId(batch.id());
  }
}
