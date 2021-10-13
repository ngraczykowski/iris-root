package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.common.model.SimpleAlertId;
import com.silenteight.payments.bridge.event.AlertInitializedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.common.resource.ResourceName;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;
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

  @Valid
  private final FircoInboundAmqpIntegrationProperties properties;
  private final AmqpInboundFactory inboundFactory;
  private final CommonChannels commonChannels;
  private final FilterAlertMessageUseCase alertMessageUseCase;

  @Bean
  IntegrationFlow messageStoredInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .transform(MessageStored.class, source ->
          new SimpleAlertId(ResourceName.create(source.getAlert()).getUuid("alert-messages"))
        )
        .filter(AlertId.class, alertId -> !alertMessageUseCase.isResolvedOrOutdated(alertId))
        .filter(AlertId.class, alertId -> !alertMessageUseCase.hasTooManyHits(alertId), spec ->
          spec.discardChannel(INT_DISCARD)
        )
        .transform(AlertId.class, source ->
            new AlertInitializedEvent(source.getAlertId())
        )
        .channel(commonChannels.alertInitialized())
        .get();
  }

  @Bean(INT_DISCARD)
  MessageChannel discardChannel() {
    return new DirectChannel();
  }

  @Bean
  IntegrationFlow discard() {
    return from(discardChannel())
        .transform(AlertId.class, alertId -> new RecommendationCompletedEvent(alertId.getAlertId()))
        .channel(commonChannels.recommendationCompleted())
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
