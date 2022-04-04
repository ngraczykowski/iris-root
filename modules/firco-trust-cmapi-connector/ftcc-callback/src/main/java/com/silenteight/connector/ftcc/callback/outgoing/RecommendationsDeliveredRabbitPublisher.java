package com.silenteight.connector.ftcc.callback.outgoing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
@Slf4j
class RecommendationsDeliveredRabbitPublisher implements RecommendationsDeliveredPublisher {

  @NonNull
  private final RabbitTemplate rabbitTemplate;
  @NonNull
  private final RecommendationsDeliveredAmqpProperties properties;

  @Override
  public void publish(RecommendationsDeliveredEvent event) {
    log.info("Sending RecommendationDelivered with {}", event);
    rabbitTemplate.convertAndSend(properties.getExchange(), "", recommendationsDelivered(event));
  }

  private static RecommendationsDelivered recommendationsDelivered(
      RecommendationsDeliveredEvent event) {
    return RecommendationsDelivered.newBuilder()
        .setBatchId(event.getBatchName())
        .setAnalysisName(event.getAnalysisName())
        .build();
  }
}
