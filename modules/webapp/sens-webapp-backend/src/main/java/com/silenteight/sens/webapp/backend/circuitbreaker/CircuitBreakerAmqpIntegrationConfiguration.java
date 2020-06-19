package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.sens.webapp.backend.circuitbreaker.CircuitBreakerIntegrationChannels.ARCHIVE_DISCREPANCIES_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CircuitBreakerDiscrepancyMessagingProperties.class)
class CircuitBreakerAmqpIntegrationConfiguration {

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  public GatewayProxyFactoryBean circuitBreakerMessageGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(CircuitBreakerMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(ARCHIVE_DISCREPANCIES_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow sendArchiveDiscrepancyIntegrationFlow(
      CircuitBreakerDiscrepancyMessagingProperties circutiBreakerMessagingProperties) {
    return flow -> flow
        .channel(ARCHIVE_DISCREPANCIES_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(circutiBreakerMessagingProperties.exchange())
            .routingKey(circutiBreakerMessagingProperties.routeArchive())
        );
  }
}
