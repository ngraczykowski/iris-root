package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;

public interface NameAddressCossmatchUseCase {

  NameAddressCrossmatchAgentResponse call(NameAddressCrossmatchAgentRequest request);
}
