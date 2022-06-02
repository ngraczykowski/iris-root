package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.integration.AgentExchangeChannels;
import com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels;
import com.silenteight.adjudication.engine.analysis.categoryrequest.integration.CategoryRequestChannels;
import com.silenteight.adjudication.engine.analysis.commentinput.integration.CommentInputChannels;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.router.HeaderValueRouter;

import javax.validation.Valid;

import static com.silenteight.adjudication.engine.analysis.analysis.integration.AnalysisChannels.ANALYSIS_ALERTS_CANCELLED_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.pii.integration.RemovePiiChannels.REMOVE_PII_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.dataset.dataset.integration.DataRetentionChannels.ALERTS_EXPIRED_INBOUND_CHANNEL;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(AnalysisInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class AnalysisInboundAmqpIntegrationConfiguration {

  @Valid
  private final AnalysisInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow analysisInboundIntegrationFlow() {
    return from(createInboundAdapter(properties.getEventInternalInboundQueueNames()))
        .log(Level.TRACE, getClass().getName() + ".analysisInboundIntegrationFlow")
        .route(consumerQueueRouter())
        .get();
  }

  @SuppressWarnings("FeatureEnvy")
  private HeaderValueRouter consumerQueueRouter() {
    var router = new HeaderValueRouter(AmqpHeaders.CONSUMER_QUEUE);

    router.setChannelMapping(
        properties.getPendingRecommendation().getInboundQueueName(),
        PendingRecommendationChannels.ADDED_ANALYSIS_ALERTS_INBOUND_CHANNEL);
    router.setChannelMapping(
        properties.getAgentExchange().getInboundQueueName(),
        AgentExchangeChannels.AGENT_EXCHANGE_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL);
    router.setChannelMapping(
        properties.getDeleteAgentExchange().getInboundQueueName(),
        AgentExchangeChannels.DELETE_AGENT_EXCHANGE_INBOUND_CHANNEL);
    router.setChannelMapping(
        properties.getCategory().getInboundQueueName(),
        CategoryRequestChannels.CATEGORY_REQUEST_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL);
    router.setChannelMapping(
        properties.getCommentInput().getInboundQueueName(),
        CommentInputChannels.COMMENT_INPUT_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL);
    router.setChannelMapping(
        properties.getAnalysisCancelledInboundQueueName(),
        AgentExchangeChannels.DELETE_AGENT_EXCHANGE_INBOUND_CHANNEL);
    return router;
  }

  @Bean
  @ConditionalOnProperty(prefix = "ae.solving", name = "enabled",
      havingValue = "false", matchIfMissing = true)
  IntegrationFlow agentResponseIntegrationFlow() {
    return from(createInboundAdapter(properties.getAgentResponseInboundQueueName()))
        .log(Level.TRACE, getClass().getName() + ".agentResponseIntegrationFlow")
        .channel(AgentResponseChannels.AGENT_RESPONSE_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  IntegrationFlow dataRetentionIntegrationFlow() {
    return from(createInboundAdapter(properties.getDataRetentionInboundQueueName()))
        .log(Level.TRACE, getClass().getName() + ".dataRetentionIntegrationFlow")
        .channel(ALERTS_EXPIRED_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  IntegrationFlow piiExpiredIntegrationFlow() {
    return from(createInboundAdapter(properties.getPiiExpiredInboundQueueName()))
        .log(Level.TRACE, getClass().getName() + ".dataRetentionIntegrationFlow")
        .channel(REMOVE_PII_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  IntegrationFlow processAnalysisCancelledIntegrationFlow() {
    return from(createInboundAdapter(properties.getAnalysisCancelledInboundQueueName()))
        .log(Level.TRACE, getClass().getName() + ".analysisCancelledIntegrationFlow")
        .channel(ANALYSIS_ALERTS_CANCELLED_INBOUND_CHANNEL)
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
