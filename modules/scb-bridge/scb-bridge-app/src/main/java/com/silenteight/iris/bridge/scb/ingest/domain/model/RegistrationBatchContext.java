/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.silenteight.iris.bridge.scb.ingest.domain.model.Batch.Priority;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public enum RegistrationBatchContext {

  CBS_CONTEXT(Priority.MEDIUM, BatchSource.CBS),
  GNS_RT_CONTEXT(Priority.HIGH, BatchSource.GNS_RT),
  LEARNING_CONTEXT(Priority.LOW, BatchSource.LEARNING);

  private final Priority priority;

  private final BatchSource batchSource;

}
