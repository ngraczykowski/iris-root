package com.silenteight.payments.bridge.firco.ingress;

import java.util.UUID;

class DefaultAlertMessageNameGenerator implements AlertMessageNameGenerator {

  @Override
  public String generateName() {
    return "alert-messages/" + UUID.randomUUID();
  }
}
