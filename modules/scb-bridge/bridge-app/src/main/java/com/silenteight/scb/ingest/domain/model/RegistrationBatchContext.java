package com.silenteight.scb.ingest.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.silenteight.scb.ingest.domain.model.Batch.Priority;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.HIGH;
import static com.silenteight.scb.ingest.domain.model.Batch.Priority.LOW;
import static com.silenteight.scb.ingest.domain.model.Batch.Priority.MEDIUM;
import static com.silenteight.scb.ingest.domain.model.BatchSource.CBS;
import static com.silenteight.scb.ingest.domain.model.BatchSource.GNS_RT;
import static com.silenteight.scb.ingest.domain.model.BatchSource.LEARNING;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public enum RegistrationBatchContext {

  CBS_CONTEXT(MEDIUM, CBS),
  GNS_RT_CONTEXT(HIGH, GNS_RT),
  LEARNING_CONTEXT(LOW, LEARNING);

  private final Priority priority;

  private final BatchSource batchSource;

}
