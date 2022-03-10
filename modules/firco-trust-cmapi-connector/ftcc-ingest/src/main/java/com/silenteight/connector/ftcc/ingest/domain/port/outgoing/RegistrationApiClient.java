package com.silenteight.connector.ftcc.ingest.domain.port.outgoing;

import com.silenteight.connector.ftcc.ingest.domain.Batch;

public interface RegistrationApiClient {

  void registerBatch(Batch batch);
}
