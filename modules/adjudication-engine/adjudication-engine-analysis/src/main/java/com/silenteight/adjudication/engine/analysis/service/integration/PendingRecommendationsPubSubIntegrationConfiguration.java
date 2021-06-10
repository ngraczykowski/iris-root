package com.silenteight.adjudication.engine.analysis.service.integration;

import com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels;
import com.silenteight.adjudication.engine.analysis.categoryrequest.integration.CategoryRequestChannels;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

import static com.silenteight.adjudication.engine.analysis.service.integration.IntegrationChannels.PENDING_RECOMMENDATIONS_PUB_SUB_CHANNEL;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
class PendingRecommendationsPubSubIntegrationConfiguration {

  @Bean
  MessageChannel pendingRecommendationsPubSubChannel() {
    return MessageChannels.publishSubscribe(PENDING_RECOMMENDATIONS_PUB_SUB_CHANNEL).get();
  }

  @Bean
  IntegrationFlow agentExchangePendingRecommendationsInboundIntegrationFlow() {
    return from(pendingRecommendationsPubSubChannel())
        .channel(AgentExchangeChannels.AGENT_EXCHANGE_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  IntegrationFlow categoryRequestPendingRecommendationsInboundIntegrationFlow() {
    return from(pendingRecommendationsPubSubChannel())
        .channel(CategoryRequestChannels.CATEGORY_REQUEST_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .get();
  }
}
