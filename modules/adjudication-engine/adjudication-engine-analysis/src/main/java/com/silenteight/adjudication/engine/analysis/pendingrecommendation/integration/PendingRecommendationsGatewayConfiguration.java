package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels.PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL;

@Configuration
class PendingRecommendationsGatewayConfiguration {

  @Bean
  GatewayProxyFactoryBean pendingRecommendationGateway() {
    var factoryBean = new GatewayProxyFactoryBean(
        PendingRecommendationGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
