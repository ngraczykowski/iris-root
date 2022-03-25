package com.silenteight.scb.ingest.domain.model;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.BatchReadEvent;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;

public record IngestBatchMessage(BatchReadEvent event, Priority priority) {
}