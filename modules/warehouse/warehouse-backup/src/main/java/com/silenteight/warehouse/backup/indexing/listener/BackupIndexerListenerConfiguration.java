package com.silenteight.warehouse.backup.indexing.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.backup.indexing.IndexerIntegrationProperties;
import com.silenteight.warehouse.backup.indexing.IndexingConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@RequiredArgsConstructor
@Import(IndexingConfiguration.class)
class BackupIndexerListenerConfiguration {

  public static final String BACKUP_INDEXING_INBOUND_CHANNEL =
      "backupIndexingInboundChannel";

  @NonNull
  private final IndexerIntegrationProperties properties;

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  private final ProductionIndexRequestCommandHandler productionIndexRequestCommandHandler;

  @Bean
  IntegrationFlow backupQueueToChannelIntegrationFlow() {
    return createInputFlow(
        BACKUP_INDEXING_INBOUND_CHANNEL,
        properties.getBackupIndexingInbound().getQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .handle(
            ProductionDataIndexRequest.class,
            (payload, headers) -> {
              productionIndexRequestCommandHandler.handle(payload);
              return null;
            })
        .get();
  }
}
