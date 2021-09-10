package com.silenteight.payments.bridge.common.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class CommonIntegrationConfiguration {

  @Bean(CommonChannels.COMMANDS_OUTBOUND_CHANNEL)
  MessageChannel commandsInboundChannel() {
    return new DirectChannel();
  }
}
