package com.silenteight.serp.governance.ingest.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;
import com.silenteight.serp.governance.common.integration.FeatureVectorSolvedAmqpIntegrationConfiguration;
import com.silenteight.serp.governance.ingest.repackager.IngestDataToSolvedEventRepackagerService;
import com.silenteight.serp.governance.ingest.repackager.IngestDataValidator;
import com.silenteight.serp.governance.policy.solve.event.FeatureVectorEventStrategyService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

import static com.silenteight.serp.governance.common.integration.FeatureVectorSolvedAmqpIntegrationConfiguration.FEATURE_VECTOR_SOLVED_OUTBOUND_CHANNEL;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(IngestEventIntegrationProperties.class)
@Import(FeatureVectorSolvedAmqpIntegrationConfiguration.class)
class IngestListenerConfiguration {

  private static final String INGEST_EVENT_INBOUND_CHANNEL = "ingestEventInboundChannel";

  public static final String INGEST_EVENT_OUTBOUND_CHANNEL
      = FEATURE_VECTOR_SOLVED_OUTBOUND_CHANNEL;

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow featureVectorIngestQueueToChannelIntegrationFlow(
      @Valid IngestEventIntegrationProperties properties) {

    return createInputFlow(
        INGEST_EVENT_INBOUND_CHANNEL, properties.getReceive());
  }

  private IntegrationFlow createInputFlow(String channel, AmqpInboundProperties properties) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(properties.getQueueName())))
        .channel(channel)
        .get();
  }

  @Bean
  IngestDataHandler ingestDataHandler(
      IngestDataToSolvedEventRepackagerService ingestDataToSolvedEventRepackagerService,
      IngestDataValidator ingestDataValidator,
      FeatureVectorEventStrategyService featureVectorEventStrategyService) {

    return new IngestDataHandler(
        ingestDataToSolvedEventRepackagerService,
        ingestDataValidator,
        featureVectorEventStrategyService);
  }

  @Bean
  IngestRecommendationIntegrationFlowAdapter ingestRecommendationIntegrationFlowAdapter(
      IngestDataHandler handler) {

    return new IngestRecommendationIntegrationFlowAdapter(
        INGEST_EVENT_INBOUND_CHANNEL,
        INGEST_EVENT_OUTBOUND_CHANNEL,
        handler);
  }
}
