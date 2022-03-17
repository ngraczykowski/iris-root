package com.silenteight.scb.feeding.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.registration.api.v1.FedMatch;
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed;
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent;
import com.silenteight.scb.feeding.domain.port.outgoing.FeedingEventPublisher;
import com.silenteight.scb.feeding.infrastructure.amqp.AmqpFeedingOutgoingMatchFeatureInputSetFedProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedingRabbitEventPublisher implements FeedingEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final AmqpFeedingOutgoingMatchFeatureInputSetFedProperties properties;

  @Override
  public void publish(UdsFedEvent event) {
    var message = MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(event.batchId())
        .setAlertId(event.alertId())
        .setAlertErrorDescription(event.errorDescription().getDescription())
        .setFeedingStatus(FeedingStatus.valueOf(event.feedingStatus().name()))
        .addAllFedMatches(createFedMatches(event.fedMatches()))
        .build();
    rabbitTemplate.convertAndSend(properties.exchangeName(), "", message);
  }

  private Iterable<FedMatch> createFedMatches(List<UdsFedEvent.FedMatch> fedMatches) {
    return fedMatches.stream()
        .map(fedMatch -> FedMatch.newBuilder()
            .setMatchId(fedMatch.matchId())
            .build())
        .toList();
  }
}
