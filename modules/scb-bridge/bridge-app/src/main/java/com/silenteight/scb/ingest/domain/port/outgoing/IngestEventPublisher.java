package com.silenteight.scb.ingest.domain.port.outgoing;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

public interface IngestEventPublisher {

  void publish(Alert alert);
}
