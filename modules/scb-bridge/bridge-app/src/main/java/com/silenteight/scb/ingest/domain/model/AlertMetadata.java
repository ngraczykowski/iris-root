package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;

public record AlertMetadata(
    String watchlistId,
    String discriminator) {

  @Builder
  public AlertMetadata {}
}