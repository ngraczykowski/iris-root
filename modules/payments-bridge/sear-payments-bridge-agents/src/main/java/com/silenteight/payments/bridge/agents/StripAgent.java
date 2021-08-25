package com.silenteight.payments.bridge.agents;

import lombok.NonNull;

class StripAgent {

  private static final String STRIP_PREFIX = "STRIP";

  @NonNull
  StripAgentResponse invoke(@NonNull String hittedEntityId) {
    return hittedEntityId.contains(STRIP_PREFIX) ? StripAgentResponse.STRIPPED
                                                 : StripAgentResponse.NOT_STRIPPED;
  }

}
