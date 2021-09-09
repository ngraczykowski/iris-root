package com.silenteight.adjudication.engine.analysis.agentresponse.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentresponse.DeleteAgentExchangeGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.DELETE_AGENT_EXCHANGE_GATEWAY_CHANNEL;

@Configuration
@RequiredArgsConstructor
class DeleteAgentExchangeGatewayIntegrationConfiguration {

  @Bean
  GatewayProxyFactoryBean deleteAgentExchangeGateway() {
    var factoryBean = new GatewayProxyFactoryBean(DeleteAgentExchangeGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(DELETE_AGENT_EXCHANGE_GATEWAY_CHANNEL);
    return factoryBean;
  }
}
