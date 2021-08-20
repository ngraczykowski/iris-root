package com.silenteight.payments.bridge.firco.ingress;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class DefaultAlertMessageNameGenerator implements AlertMessageNameGenerator {

  @Override
  public String generateName() {
    return "alert-messages/" + UUID.randomUUID();
  }
}
