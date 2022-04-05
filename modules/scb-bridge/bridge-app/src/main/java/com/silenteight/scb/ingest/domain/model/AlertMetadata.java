package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;

public record AlertMetadata(
    String watchlistId,
    String discriminator,
    String systemId) {

  @Builder
  public AlertMetadata {}
}
