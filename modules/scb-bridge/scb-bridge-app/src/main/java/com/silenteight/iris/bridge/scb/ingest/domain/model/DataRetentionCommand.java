/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.Builder;

import java.util.List;

public record DataRetentionCommand(List<DataRetentionAlert> alerts) {

  public static record DataRetentionAlert(
      String systemId,
      String internalBatchId) {

    @Builder
    public DataRetentionAlert {}
  }
}
