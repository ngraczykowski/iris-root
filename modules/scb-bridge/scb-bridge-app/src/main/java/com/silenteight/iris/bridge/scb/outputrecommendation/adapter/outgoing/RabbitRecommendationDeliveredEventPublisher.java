/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.port.outgoing.RecommendationDeliveredEventPublisher;
import com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.amqp.OutputRecommendationDeliveredProperties;
import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsDeliveredEvent;

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
    var message = RecommendationsDelivered.newBuilder()
        .setBatchId(event.batchId())
        .setAnalysisName(event.analysisName())
        .build();

    log.info("Send recommendations delivered message for batchId: {}", event.batchId());
    rabbitTemplate.convertAndSend(properties.exchangeName(), "", message);
  }
}
