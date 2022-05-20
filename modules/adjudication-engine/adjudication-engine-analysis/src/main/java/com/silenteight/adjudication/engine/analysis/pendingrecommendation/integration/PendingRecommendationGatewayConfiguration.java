package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.messaging.MessageChannel;

import static com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels.PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL;

@Configuration
class PendingRecommendationGatewayConfiguration {

  @Bean
  MessageChannel pendingRecommendationsOutboundChannel() {
    return MessageChannels.direct(PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL).get();
  }

  @Bean
  GatewayProxyFactoryBean pendingRecommendationGateway() {
    var factoryBean = new GatewayProxyFactoryBean(PendingRecommendationGateway.class);
    factoryBean.setDefaultRequestChannel(pendingRecommendationsOutboundChannel());
    return factoryBean;
  }
}
