/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId;

import java.time.Instant;

public record Decision(
    ObjectId id,
    AnalystSolution solution,
    String comment,
    String authorId,
    String stateName,
    Instant createdAt) {

  @Builder
  public Decision {}

  public enum AnalystSolution {
    ANALYST_NO_SOLUTION(0),
    ANALYST_FALSE_POSITIVE(1),
    ANALYST_POTENTIAL_TRUE_POSITIVE(2),
    ANALYST_TRUE_POSITIVE(3),
    ANALYST_OTHER(4);

    @Getter
    private final int value;

    AnalystSolution(int value) {
      this.value = value;
    }
  }
}
