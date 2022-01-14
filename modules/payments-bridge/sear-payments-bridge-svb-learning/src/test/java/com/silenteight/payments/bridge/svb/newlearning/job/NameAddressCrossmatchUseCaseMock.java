package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;

public class NameAddressCrossmatchUseCaseMock implements NameAddressCrossmatchUseCase {

  @Override
  public NameAddressCrossmatchAgentResponse call(
      NameAddressCrossmatchAgentRequest request) {
    return NameAddressCrossmatchAgentResponse.CROSSMATCH;
  }
}
