package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.beans.factory.annotation.Value;
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
      @Value("${messaging.exchange.bulk-change}") String bulkChangeExchange,
      @Value("${messaging.route.bulk-change.create}") String bulkChangeExchangeCreateRoute) {
    return flow -> flow
        .channel(CREATE_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(bulkChangeExchange)
            .routingKey(bulkChangeExchangeCreateRoute)
        );
  }

  @Bean
  IntegrationFlow sendApplyBulkChangeIntegrationFlow(
      @Value("${messaging.exchange.bulk-change}") String bulkChangeExchange,
      @Value("${messaging.route.bulk-change.apply}") String bulkChangeExchangeApplyRoute) {
    return flow -> flow
        .channel(APPLY_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(bulkChangeExchange)
            .routingKey(bulkChangeExchangeApplyRoute)
        );
  }

  @Bean
  IntegrationFlow sendRejectBulkChangeIntegrationFlow(
      @Value("${messaging.exchange.bulk-change}") String bulkChangeExchange,
      @Value("${messaging.route.bulk-change.reject}") String bulkChangeExchangeRejectRoute) {
    return flow -> flow
        .channel(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(bulkChangeExchange)
            .routingKey(bulkChangeExchangeRejectRoute)
        );
  }
}
