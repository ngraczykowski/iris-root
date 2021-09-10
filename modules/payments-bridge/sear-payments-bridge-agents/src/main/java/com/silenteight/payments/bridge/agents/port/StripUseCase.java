package com.silenteight.payments.bridge.agents.port;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.StripAgentResponse;

public interface StripUseCase {

  StripAgentResponse invoke(@NonNull String hittedEntityId);
}
