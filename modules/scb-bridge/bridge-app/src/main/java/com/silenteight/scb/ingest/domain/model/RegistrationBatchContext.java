package com.silenteight.scb.ingest.domain.model;

import com.silenteight.scb.ingest.domain.model.Batch.Priority;

public record RegistrationBatchContext(Priority priority, BatchSource batchSource) {
}
