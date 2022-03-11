package com.silenteight.scb.ingest.adapter.incomming.common.model;

import lombok.Builder;

import java.util.UUID;

public record ObjectId(
    UUID id,
    String discriminator,
    String sourceId) {

  @Builder(toBuilder = true)
  public ObjectId {}
}