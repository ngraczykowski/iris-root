package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.app.integration.callback.AlertDeliveredIntegrationService;
import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.common.model.SimpleAlertId;
import com.silenteight.payments.bridge.common.resource.ResourceName;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.port.SendResponseUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

import java.util.UUID;
import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;
import static org.springframework.integration.handler.LoggingHandler.Level.INFO;

@Configuration
@EnableConfigurationProperties(FircoInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class FircoInboundAmqpIntegrationConfiguration {

  private static final String INT_DISCARD =
      "FircoInboundAmqpIntegrationConfiguration_int_discardChannel";
  private static final String RESPONSE_COMPLETED_INT_DISCARD =
      "FircoInboundAmqpIntegrationConfiguration_int_responseCompletedDiscardChannel";

  @Valid
  private final FircoInboundAmqpIntegrationProperties properties;
  private final AmqpInboundFactory inboundFactory;
  private final FilterAlertMessageUseCase filterAlertMessageUseCase;
  private final SendResponseUseCase sendResponseUseCase;
  private final CreateRecommendationUseCase createRecommendationUseCase;
  private final AlertDeliveredIntegrationService alertDeliveredIntegrationService;


  @Bean(INT_DISCARD)
  MessageChannel discardChannel() {
    return new DirectChannel();
  }

  @Bean
  IntegrationFlow discard() {
    return from(discardChannel())
        .transform(AlertId.class, alertId -> new BridgeSourcedRecommendation(
            alertId.getAlertId(),
            AlertMessageStatus.RECOMMENDED.name(),
            RecommendationReason.TOO_MANY_HITS.name()))
        .handle(BridgeSourcedRecommendation.class, (payload, headers) -> {
          createRecommendationUseCase.create(payload);
          return null;
        })
        .get();
  }

  @Bean
  IntegrationFlow responseCompletedInbound() {
    return from(createInboundAdapter(properties.getCommands().getResponseCompletedQueueName()))
        .log(INFO, FircoInboundAmqpIntegrationConfiguration.class.toString(), msg ->
            "Received response-completed msg for: " + buildAlertId(
                (ResponseCompleted) msg.getPayload()).getAlertId())
        .filter(
            ResponseCompleted.class,
            source -> !filterAlertMessageUseCase.isOutdated(buildAlertId(source)),
            spec -> spec.discardChannel(RESPONSE_COMPLETED_INT_DISCARD))
        .handle(ResponseCompleted.class, (payload, headers) -> {
          sendResponseUseCase.send(payload);
          return null;
        }).get();
  }

  private AlertId buildAlertId(ResponseCompleted responseCompleted) {
    return new SimpleAlertId(ResourceName.create(
        responseCompleted.getAlert()).getUuid("alerts"));
  }

  @Bean(RESPONSE_COMPLETED_INT_DISCARD)
  MessageChannel responseCompletedDiscardChannel() {
    return new DirectChannel();
  }

  @Bean
  IntegrationFlow responseCompletedDiscard() {
    return from(responseCompletedDiscardChannel())
        .handle(ResponseCompleted.class, (payload, headers) -> {
          alertDeliveredIntegrationService.sendUndelivered(
              buildAlertId(payload).getAlertId(), extractStatus(payload));
          return null;
        })
        .get();
  }

  private AlertMessageStatus extractStatus(ResponseCompleted responseCompleted) {
    return AlertMessageStatus.valueOf(
        ResourceName.create(responseCompleted.getStatus()).get("status"));
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(
            c -> c.addQueueNames(queueNames).maxConcurrentConsumers(1).prefetchCount(25));
  }
}
