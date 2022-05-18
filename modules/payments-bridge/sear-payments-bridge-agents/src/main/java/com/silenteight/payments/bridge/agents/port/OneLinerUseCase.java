package com.silenteight.payments.bridge.agents.port;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.OneLinerAgentRequest;
import com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse;

public interface OneLinerUseCase {

  OneLinerAgentResponse invoke(@NonNull OneLinerAgentRequest oneLinerAgentRequest);
}
