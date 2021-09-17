package com.silenteight.payments.bridge.firco.callback;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.integration.AlertMessageChannels;
import com.silenteight.payments.bridge.firco.dto.output.ClientRequestDto;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@EnableConfigurationProperties(CallbackRequestProperties.class)
@Configuration
@RequiredArgsConstructor
class OutboundGatewayConfiguration {

  private final CallbackRequestFactory callbackRequestFactory;

  @Bean
  IntegrationFlow outboundCallbackGatewayIntegrationFlow() {
    return from(AlertMessageChannels.ALERT_MESSAGE_RESPONSE_OUTBOUND_CHANNEL)
        .handle(ClientRequestDto.class, (payload, headers) -> {
          callbackRequestFactory.create(payload).invoke();
          return null;
        })
        .get();
  }

}
