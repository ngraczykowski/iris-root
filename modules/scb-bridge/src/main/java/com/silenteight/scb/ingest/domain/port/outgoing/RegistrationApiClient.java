package com.silenteight.scb.ingest.domain.port.outgoing;

import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

public interface RegistrationApiClient {

  void registerBatch(Batch batch);

  RegistrationResponse registerAlertsAndMatches(RegistrationRequest request);
}
