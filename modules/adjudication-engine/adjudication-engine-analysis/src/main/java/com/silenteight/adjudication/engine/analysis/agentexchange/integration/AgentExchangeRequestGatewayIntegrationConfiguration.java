package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeRequestGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@RequiredArgsConstructor
@Configuration
class AgentExchangeRequestGatewayIntegrationConfiguration {

  @Bean
  GatewayProxyFactoryBean agentExchangeRequestGateway() {
    var factoryBean = new GatewayProxyFactoryBean(AgentExchangeRequestGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(
        AgentExchangeChannels.AGENT_EXCHANGE_REQUEST_GATEWAY_CHANNEL);
    return factoryBean;
  }
}
