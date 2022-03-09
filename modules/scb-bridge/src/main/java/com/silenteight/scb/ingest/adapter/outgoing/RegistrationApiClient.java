package com.silenteight.scb.ingest.adapter.outgoing;

import com.silenteight.scb.ingest.domain.model.Batch;

public interface RegistrationApiClient {

  void registerBatch(Batch batch);

}
