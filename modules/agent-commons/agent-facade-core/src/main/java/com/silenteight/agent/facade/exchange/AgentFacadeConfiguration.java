package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.monitoring.Monitoring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty("agent.facade.enabled")
class AgentFacadeConfiguration {

  public static final String INBOUND_CHANNEL_NAME = "inboundChannel";
  public static final String OUTBOUND_CHANNEL_NAME = "outboundChannel";

  @Bean
  MessageTransformer messageTransformer(Monitoring monitoring) {
    return new MessageTransformer(monitoring);
  }
}
