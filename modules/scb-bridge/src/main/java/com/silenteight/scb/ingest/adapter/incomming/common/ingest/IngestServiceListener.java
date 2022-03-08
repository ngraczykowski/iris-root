package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.proto.serp.v1.alert.Alert;

public interface IngestServiceListener {
  void send(Alert alert);
}
