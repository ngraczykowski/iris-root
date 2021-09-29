package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.firco.alertmessage.port.IssueRecommendationUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(FircoInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class FircoInboundAmqpIntegrationConfiguration {

  @Valid
  private final FircoInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;

  private final IssueRecommendationUseCase issueRecommendationUseCase;

  private final CommonChannels commonChannels;

  @Bean
  IntegrationFlow alertMessageStoredReceivedInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
       .handle(MessageStored.class, (payload, headers) -> {
         // TODO: send back recommendation immediately (temporary)
         issueRecommendationUseCase.issue(payload);
         return null;
       })
       // .transform(MessageStored.class, source ->
       //     new AlertDelivered(ResourceName.create(source.getAlert()).get("alert-messages"))
       // )
       // .channel(commonChannels.alertDelivered())
       .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
