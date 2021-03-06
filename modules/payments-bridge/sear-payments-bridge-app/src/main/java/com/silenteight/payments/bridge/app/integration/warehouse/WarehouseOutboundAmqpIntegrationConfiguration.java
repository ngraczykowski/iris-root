package com.silenteight.payments.bridge.app.integration.warehouse;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.warehouse.index.model.WarehouseIndexRequestedEvent;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.channel.DirectChannel;
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
@Slf4j
@Profile("!mockwarehouse")
class WarehouseOutboundAmqpIntegrationConfiguration {
  static final String WAREHOUSE_UPDATE_OUTBOUND = "warehouseOutboundChannel";

  @Valid
  private final WarehouseOutboundAmqpIntegrationProperties properties;
  private final AmqpOutboundFactory outboundFactory;
  private final Timer executionTimer;

  WarehouseOutboundAmqpIntegrationConfiguration(
          final WarehouseOutboundAmqpIntegrationProperties properties,
          final AmqpOutboundFactory outboundFactory,
          final MeterRegistry meterRegistry
  ) {
    this.properties = properties;
    this.outboundFactory = outboundFactory;

    final Timer timer = Timer.builder("message.store.in.warehouse.outbound")
            .publishPercentileHistogram()
            .publishPercentiles(0.5, 0.95, 0.99)
            .description("How many times process message store in warehouse")
            .register(meterRegistry);
    this.executionTimer = timer;
  }


  @Bean
  IntegrationFlow messageStoredInWarehouseOutboundFlow() {
    return createOutboundFlow(
        WAREHOUSE_UPDATE_OUTBOUND,
        properties.getCommand().getOutboundExchangeName(),
        properties.getCommand().getAlertStoredRoutingKey());
  }

  private StandardIntegrationFlow createOutboundFlow(
      String outboundChannel, String outboundExchangeName, String outboundRoutingKey) {

    return from(outboundChannel)
        .enrichHeaders(h -> h.priorityFunction(createPriorityFunction()))
        .transform(WarehouseIndexRequestedEvent.class, WarehouseIndexRequestedEvent::getRequest)
        .handle(ProductionDataIndexRequest.class, (payload, headers) -> {
          final Timer.Sample start = Timer.start();
          log.info("Sending alerts (discriminators: [" +
              payload.getAlertsList()
                  .stream().map(Alert::getDiscriminator)
                  .collect(Collectors.joining(","))
              + "]) to data warehouse.");

          start.stop(this.executionTimer);
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

  @Bean(WAREHOUSE_UPDATE_OUTBOUND)
  MessageChannel warehouseOutbound() {
    return new DirectChannel();
  }

}
