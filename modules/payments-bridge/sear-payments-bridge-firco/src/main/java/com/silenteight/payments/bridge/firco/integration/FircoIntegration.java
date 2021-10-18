package com.silenteight.payments.bridge.firco.integration;


import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertRegistration;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import static com.silenteight.payments.bridge.firco.integration.FircoIntegrationChannels.ALERT_REGISTERED_REQUEST_CHANNEL;
import static com.silenteight.payments.bridge.firco.integration.FircoIntegrationChannels.ALERT_REGISTERED_RESPONSE_CHANNEL;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@RequiredArgsConstructor
class FircoIntegration {

  private final AlertMessageUseCase alertMessageUseCase;

  @Bean
  IntegrationFlow alertRegistrationFlow() {
    return from(ALERT_REGISTERED_REQUEST_CHANNEL)
        .handle(AlertRegistration.class,
            (payload, headers) -> alertMessageUseCase.exists(payload)
        )
        .channel(ALERT_REGISTERED_RESPONSE_CHANNEL)
        .get();
  }
}
