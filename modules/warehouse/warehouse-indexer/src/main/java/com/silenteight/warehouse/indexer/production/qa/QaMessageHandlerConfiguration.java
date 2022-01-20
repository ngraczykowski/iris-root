package com.silenteight.warehouse.indexer.production.qa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.query.single.AlertQueryService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableConfigurationProperties(QaIndexerProperties.class)
@RequiredArgsConstructor
public class QaMessageHandlerConfiguration {

  public static final String QA_INDEXING_INBOUND_CHANNEL = "qaIndexingInboundChannel";
  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  private final QaIndexerProperties properties;

  @Bean
  QaAlertIndexUseCase qaAlertIndexUseCase(
      QaAlertMappingService qaAlertMappingService,
      QaAlertIndexResolvingService qaAlertIndexResolvingService,
      AlertIndexService alertIndexService) {

    return new QaAlertIndexUseCase(
        qaAlertMappingService,
        qaAlertIndexResolvingService,
        alertIndexService,
        properties.getQaBatchSize());
  }

  @Bean
  IntegrationFlow qaIndexingQueueToChannelIntegrationFlow() {
    return createInputFlow(
        QA_INDEXING_INBOUND_CHANNEL,
        properties.getQaIndexingInbound().getQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }

  @Bean
  QaRequestCommandIntegrationFlowAdapter qaRequestCommandIntegrationFlowAdapter(
      QaIndexRequestCommandHandler qaIndexRequestCommandHandler) {

    return new QaRequestCommandIntegrationFlowAdapter(
        qaIndexRequestCommandHandler,
        QA_INDEXING_INBOUND_CHANNEL);
  }

  @Bean
  QaAlertIndexResolvingService qaProductionAlertIndexResolvingService(
      AlertQueryService alertQueryService,
      RestHighLevelClient restHighLevelAdminClient,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {

    return new QaAlertIndexResolvingService(alertQueryService,
        restHighLevelAdminClient,
        productionSearchRequestBuilder);
  }

  @Bean
  QaAlertMappingService qaAlertMappingService() {
    return new QaAlertMappingService();
  }
}
