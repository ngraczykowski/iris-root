package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

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
class BulkChangeAmqpIntegrationConfiguration {

  private static final String EXCHANGE_BULK_CHANGE = "bulk-change";
  private static final String ROUTE_BULK_CHANGE_CREATE = "bulk-change.create";
  private static final String ROUTE_BULK_CHANGE_APPLY = "bulk-change.apply";
  private static final String ROUTE_BULK_CHANGE_REJECT = "bulk-change.reject";

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
  IntegrationFlow sendCreateBulkChangeIntegrationFlow() {
    return flow -> flow
        .channel(CREATE_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(EXCHANGE_BULK_CHANGE)
            .routingKey(ROUTE_BULK_CHANGE_CREATE)
        );
  }

  @Bean
  IntegrationFlow sendApplyBulkChangeIntegrationFlow() {
    return flow -> flow
        .channel(APPLY_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(EXCHANGE_BULK_CHANGE)
            .routingKey(ROUTE_BULK_CHANGE_APPLY)
        );
  }

  @Bean
  IntegrationFlow sendRejectBulkChangeIntegrationFlow() {
    return flow -> flow
        .channel(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(EXCHANGE_BULK_CHANGE)
            .routingKey(ROUTE_BULK_CHANGE_REJECT)
        );
  }
}
