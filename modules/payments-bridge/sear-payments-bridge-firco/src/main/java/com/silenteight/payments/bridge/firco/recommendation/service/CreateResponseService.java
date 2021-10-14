package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateResponseUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateResponseService implements CreateResponseUseCase {

  private final CommonChannels commonChannels;

  @Override
  public void createResponse(UUID alertId, UUID recommendationId, AlertMessageStatus status) {
    var responseCompleted = ResponseCompleted.newBuilder()
        .setAlert("alerts/" + alertId)
        .setStatus("alerts/" + alertId + "/status/" + status.name())
        .setRecommendation("alerts/" + alertId + "/recommendations/" + recommendationId.toString())
        .build();

    commonChannels.responseCompletedOutbound()
        .send(MessageBuilder.withPayload(responseCompleted).build());
  }

}
