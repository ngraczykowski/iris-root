/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model;

import lombok.Builder;

import java.util.UUID;

public record ObjectId(
    UUID id,
    String discriminator,
    String sourceId) {

  @Builder(toBuilder = true)
  public ObjectId {}
}
