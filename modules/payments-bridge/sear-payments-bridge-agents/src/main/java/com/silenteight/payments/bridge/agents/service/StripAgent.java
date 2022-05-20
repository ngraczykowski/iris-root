package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.StripAgentResponse;
import com.silenteight.payments.bridge.agents.port.StripUseCase;

import org.springframework.stereotype.Service;

@Service
class StripAgent implements StripUseCase {

  private static final String STRIP_PREFIX = "STRIP";

  @NonNull
  public StripAgentResponse invoke(@NonNull String hittedEntityId) {
    return hittedEntityId.contains(STRIP_PREFIX) ? StripAgentResponse.STRIPPED
                                                 : StripAgentResponse.NOT_STRIPPED;
  }

}
