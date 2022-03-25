package com.silenteight.scb.ingest.domain.port.outgoing;

import com.silenteight.scb.ingest.domain.model.IngestBatchMessage;

public interface IngestBatchEventPublisher {

  void publish(IngestBatchMessage batchMessage);
}
