/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.port.outgoing;

import com.silenteight.iris.bridge.scb.ingest.domain.model.Batch;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse;

public interface RegistrationApiClient {

  void registerBatch(Batch batch);

  RegistrationResponse registerAlertsAndMatches(RegistrationRequest request);
}
