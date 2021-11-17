package com.silenteight.payments.bridge.app.integration.register;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.common.model.SimpleAlertId;
import com.silenteight.payments.bridge.event.AlertInitializedEvent;
import com.silenteight.payments.bridge.event.AlertUndeliveredEvent;
import com.silenteight.payments.bridge.event.EventPublisher;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.port.SendResponseUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.common.resource.ResourceName;
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

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

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
  private final EventPublisher eventPublisher;

  @Bean
  IntegrationFlow messageStoredInbound() {
    return from(createInboundAdapter(properties.getCommands().getInboundQueueName()))
        .transform(MessageStored.class, source ->
          new SimpleAlertId(ResourceName.create(source.getAlert()).getUuid("alert-messages"))
        )
        .filter(AlertId.class, alertId -> !filterAlertMessageUseCase.isResolvedOrOutdated(alertId))
        .filter(AlertId.class, alertId -> !filterAlertMessageUseCase.hasTooManyHits(alertId),
            spec -> spec.discardChannel(INT_DISCARD)
        )
        .transform(AlertId.class, source ->
            new AlertInitializedEvent(source.getAlertId())
        )
        .handle(AlertInitializedEvent.class, (payload, headers) -> {
          eventPublisher.send(payload);
          return null;
        })
        .get();
  }

  @Bean(INT_DISCARD)
  MessageChannel discardChannel() {
    return new DirectChannel();
  }

  @Bean
  IntegrationFlow discard() {
    return from(discardChannel())
        .transform(AlertId.class, alertId -> RecommendationCompletedEvent.fromBridge(
            alertId.getAlertId(),
            AlertMessageStatus.RECOMMENDED.name(),
            RecommendationReason.TOO_MANY_HITS.name()))
        .channel(RecommendationCompletedEvent.CHANNEL)
        .get();
  }

  @Bean
  IntegrationFlow responseCompletedInbound() {
    return from(createInboundAdapter(properties.getCommands().getResponseCompletedQueueName()))
        .filter(ResponseCompleted.class,
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
        .transform(ResponseCompleted.class, source ->
          new AlertUndeliveredEvent(
              buildAlertId(source).getAlertId(),
              extractStatus(source).name()))
        .channel(AlertUndeliveredEvent.CHANNEL)
        .get();
  }

  private AlertMessageStatus extractStatus(ResponseCompleted responseCompleted) {
    return AlertMessageStatus.valueOf(
        ResourceName.create(responseCompleted.getStatus()).get("status"));
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
