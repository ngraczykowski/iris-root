package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIntegrationChannels.APPLY_BULK_CHANGE_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIntegrationChannels.CREATE_BULK_CHANGE_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIntegrationChannels.REJECT_BULK_CHANGE_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(BulkChangeMessagingProperties.class)
class BulkChangeAmqpIntegrationConfiguration {

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  public GatewayProxyFactoryBean createBulkChangeMessageGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(CreateBulkChangeMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(CREATE_BULK_CHANGE_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow sendCreateBulkChangeIntegrationFlow(
      BulkChangeMessagingProperties bulkChangeMessagingProperties) {
    return flow -> flow
        .channel(CREATE_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(bulkChangeMessagingProperties.exchange())
            .routingKey(bulkChangeMessagingProperties.routeCreate())
        );
  }

  @Bean
  IntegrationFlow sendApplyBulkChangeIntegrationFlow(
      BulkChangeMessagingProperties bulkChangeMessagingProperties) {
    return flow -> flow
        .channel(APPLY_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(bulkChangeMessagingProperties.exchange())
            .routingKey(bulkChangeMessagingProperties.routeApply())
        );
  }

  @Bean
  IntegrationFlow sendRejectBulkChangeIntegrationFlow(
      BulkChangeMessagingProperties bulkChangeMessagingProperties) {
    return flow -> flow
        .channel(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(bulkChangeMessagingProperties.exchange())
            .routingKey(bulkChangeMessagingProperties.routeReject())
        );
  }
}
