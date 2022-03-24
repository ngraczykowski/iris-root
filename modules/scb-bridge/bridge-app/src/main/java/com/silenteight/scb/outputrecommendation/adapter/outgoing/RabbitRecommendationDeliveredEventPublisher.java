package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.recommendation.api.v1.BatchDelivered;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsDeliveredEvent;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationDeliveredEventPublisher;
import com.silenteight.scb.outputrecommendation.infrastructure.amqp.OutputRecommendationDeliveredProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RabbitRecommendationDeliveredEventPublisher implements
    RecommendationDeliveredEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final OutputRecommendationDeliveredProperties properties;

  @Override
  public void publish(RecommendationsDeliveredEvent event) {
    var message = BatchDelivered.newBuilder()
        .setBatchId(event.batchId())
        .setAnalysisName(event.analysisName())
        .addAllAlertNames(event.alertNames())
        .build();

    log.info("Send recommendations delivered message for batchId: {}", event.batchId());
    rabbitTemplate.convertAndSend(properties.exchangeName(), "", message);
  }
}
