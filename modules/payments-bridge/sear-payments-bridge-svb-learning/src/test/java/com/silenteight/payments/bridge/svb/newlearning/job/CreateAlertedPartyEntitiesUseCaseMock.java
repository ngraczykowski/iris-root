package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;

import java.util.Map;

class CreateAlertedPartyEntitiesUseCaseMock implements CreateAlertedPartyEntitiesUseCase {

  @Override
  public Map<AlertedPartyKey, String> create(CreateAlertedPartyEntitiesRequest request) {
    return Map.of(AlertedPartyKey.ALERTED_ADDRESS_KEY, "Address");
  }
}
