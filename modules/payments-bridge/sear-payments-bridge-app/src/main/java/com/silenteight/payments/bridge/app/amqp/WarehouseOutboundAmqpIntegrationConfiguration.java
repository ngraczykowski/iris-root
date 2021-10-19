package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;

import java.util.stream.Collectors;
import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(WarehouseOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class WarehouseOutboundAmqpIntegrationConfiguration {

  @Valid
  private final WarehouseOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  private final CommonChannels commonChannels;

  @Bean
  IntegrationFlow messageStoredInWarehouseOutboundFlow() {
    return createOutboundFlow(
        commonChannels.warehouseRequested(),
        properties.getCommand().getOutboundExchangeName(),
        properties.getCommand().getAlertStoredRoutingKey());
  }

  private StandardIntegrationFlow createOutboundFlow(
      MessageChannel outboundChannel, String outboundExchangeName, String outboundRoutingKey) {

    return from(outboundChannel)
        .handle(ProductionDataIndexRequest.class, (payload, headers) -> {
          log.info("Sending alerts (discriminators: [" +
              payload.getAlertsList()
                  .stream().map(Alert::getDiscriminator)
                  .collect(Collectors.joining(","))
              + "]) to data warehouse.");
          return payload;
        })
        .log(Level.TRACE, getClass().getName() + "." + outboundExchangeName)
        .handle(createOutboundAdapter(outboundExchangeName, outboundRoutingKey))
        .get();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> createOutboundAdapter(
      String outboundExchangeName, String outboundRoutingKey) {

    return outboundFactory
        .outboundAdapter()
        .exchangeName(outboundExchangeName)
        .routingKey(outboundRoutingKey);
  }
}
