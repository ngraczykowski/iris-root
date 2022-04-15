package com.silenteight.serp.governance.policy.solve.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.serp.governance.common.integration.FeatureVectorSolvedAmqpIntegrationConfiguration.FEATURE_VECTOR_SOLVED_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
class FeatureVectorSolvedEventEmitterConfiguration {

  @Bean
  GatewayProxyFactoryBean createFeatureVectorSolvedMessageGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(FeatureVectorSolvedMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(FEATURE_VECTOR_SOLVED_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
