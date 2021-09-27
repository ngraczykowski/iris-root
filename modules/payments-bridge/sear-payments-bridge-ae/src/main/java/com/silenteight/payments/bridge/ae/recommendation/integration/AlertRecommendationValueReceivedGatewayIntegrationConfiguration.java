package com.silenteight.payments.bridge.ae.recommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.recommendation.port.AlertRecommendationValueReceivedGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.payments.bridge.ae.recommendation.integration.RecommendationChannels.ALERT_RECOMMENDATION_GATEWAY_CHANNEL;

@RequiredArgsConstructor
@Configuration
class AlertRecommendationValueReceivedGatewayIntegrationConfiguration {

  @Bean
  GatewayProxyFactoryBean alertRecommendationValueReceivedGateway() {
    var factoryBean = new GatewayProxyFactoryBean(AlertRecommendationValueReceivedGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(
        ALERT_RECOMMENDATION_GATEWAY_CHANNEL);
    return factoryBean;
  }
}
