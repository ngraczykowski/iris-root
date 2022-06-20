/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.Builder;

public record AlertMetadata(
    String watchlistId,
    String discriminator,
    String systemId,
    String batchId) {

  @Builder
  public AlertMetadata {}
}
