package com.silenteight.customerbridge.common.ingest;

import com.silenteight.proto.serp.v1.alert.Alert;

public interface IngestServiceListener {
  void send(Alert alert);
}
