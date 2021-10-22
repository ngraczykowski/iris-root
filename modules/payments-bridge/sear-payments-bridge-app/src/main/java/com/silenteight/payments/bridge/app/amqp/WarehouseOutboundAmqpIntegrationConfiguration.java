package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.WarehouseIndexRequestedEvent;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.function.Function;
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
        commonChannels.warehouseOutbound(),
        properties.getCommand().getOutboundExchangeName(),
        properties.getCommand().getAlertStoredRoutingKey());
  }

  private StandardIntegrationFlow createOutboundFlow(
      MessageChannel outboundChannel, String outboundExchangeName, String outboundRoutingKey) {

    return from(outboundChannel)
        .enrichHeaders(h -> h.priorityFunction(createPriorityFunction()))
        .transform(WarehouseIndexRequestedEvent.class, WarehouseIndexRequestedEvent::getRequest)
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

  private Function<Message<WarehouseIndexRequestedEvent>, Object> createPriorityFunction() {
    return message -> {
      var payload = message.getPayload();
      var priority = properties.getPriority(payload.getIndexRequestOrigin());
      log.debug("Setting priority to: {} to index-request: {}",
          priority, payload.getRequest().getRequestId());
      return priority;
    };
  }

}
