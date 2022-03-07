package com.silenteight.fab.dataprep.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.UdsFedEvent;
import com.silenteight.fab.dataprep.domain.outgoing.FeedingEventPublisher;
import com.silenteight.fab.dataprep.infrastructure.amqp.AmqpFeedingOutgoingMatchFeatureInputSetFedProperties;
import com.silenteight.proto.registration.api.v1.FedMatch;
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed;
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class FeedingRabbitEventPublisher implements FeedingEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final AmqpFeedingOutgoingMatchFeatureInputSetFedProperties properties;

  @Override
  public void publish(UdsFedEvent event) {
    var message = MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(event.getBatchId())
        .setAlertId(event.getAlertId())
        .setAlertErrorDescription(event.getErrorDescription().getDescription())
        .setFeedingStatus(FeedingStatus.valueOf(event.getFeedingStatus().name()))
        .addAllFedMatches(createFedMatches(event.getFedMatches()))
        .build();

    rabbitTemplate.convertAndSend(properties.getExchangeName(), "", message);
  }

  private Iterable<FedMatch> createFedMatches(List<UdsFedEvent.FedMatch> fedMatches) {
    return fedMatches.stream()
        .map(fedMatch -> FedMatch.newBuilder()
            .setMatchId(fedMatch.getMatchId())
            .build())
        .collect(toList());
  }
}
