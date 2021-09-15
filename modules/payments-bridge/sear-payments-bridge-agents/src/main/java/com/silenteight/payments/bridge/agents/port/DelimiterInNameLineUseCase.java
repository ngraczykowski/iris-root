package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentRequest;
import com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentResponse;

public interface DelimiterInNameLineUseCase {

  DelimiterInNameLineAgentResponse invoke(
      DelimiterInNameLineAgentRequest delimiterInNameLineAgentRequest);
}
