package com.silenteight.payments.bridge.governance.core.solvingmodel.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.governance.core.solvingmodel.port.ModelPromotedToProductionReceivedGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.payments.bridge.governance.core.solvingmodel.integration.SolvingModelChannels.SOLVING_MODEL_GATEWAY_CHANNEL;

@RequiredArgsConstructor
@Configuration
class ModelPromotedToProductionReceivedGatewayIntegrationConfiguration {

  @Bean
  GatewayProxyFactoryBean modelPromotedToProductionReceivedGateway() {
    var factoryBean = new GatewayProxyFactoryBean(ModelPromotedToProductionReceivedGateway.class);
    factoryBean.setDefaultRequestChannelName(SOLVING_MODEL_GATEWAY_CHANNEL);
    return factoryBean;
  }
}
