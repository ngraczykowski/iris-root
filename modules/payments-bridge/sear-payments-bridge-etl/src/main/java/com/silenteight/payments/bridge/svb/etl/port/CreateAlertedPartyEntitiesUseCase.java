package com.silenteight.payments.bridge.svb.etl.port;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.svb.etl.model.CreateAlertedPartyEntitiesRequest;

import java.util.Map;

public interface CreateAlertedPartyEntitiesUseCase {

  Map<AlertedPartyKey, String> create(CreateAlertedPartyEntitiesRequest request);
}
