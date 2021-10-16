package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.recommendation.port.NotifyResponseCompletedUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
class NotifyResponseCompletedService implements NotifyResponseCompletedUseCase {

  private final CommonChannels commonChannels;

  @Override
  public void notify(UUID alertId, UUID recommendationId, AlertMessageStatus status) {
    var responseCompleted = ResponseCompleted.newBuilder()
        .setAlert("alerts/" + alertId)
        .setStatus("alerts/" + alertId + "/status/" + status.name())
        .setRecommendation("alerts/" + alertId + "/recommendations/" + recommendationId)
        .build();

    commonChannels.responseCompletedOutbound()
        .send(MessageBuilder.withPayload(responseCompleted).build());
  }
}
