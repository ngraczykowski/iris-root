package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentRequest;
import com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentResponse;

public interface MatchTextFirstTokenOfAddressUseCase {

  MatchtextFirstTokenOfAddressAgentResponse invoke(
      MatchtextFirstTokenOfAddressAgentRequest matchtextFirstTokenOfAddressAgentRequest);
}
