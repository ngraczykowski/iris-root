package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.SimpleAlertId;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AcceptAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.port.CreateResponseUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;

@RequiredArgsConstructor
@Slf4j
@MessageEndpoint
class RecommendationCompletedEndpoint {

  private final AlertMessageStatusService alertMessageStatusService;
  private final AcceptAlertMessageUseCase alertMessageUseCase;
  private final CreateResponseUseCase createResponseUseCase;

  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  public void accept(RecommendationCompletedEvent recommendation) {
    var alertId = recommendation.getAlertId();

    if (!alertMessageUseCase.test(new SimpleAlertId(alertId))) {
      return;
    }
    /*
     * The request isn't sent to AE at version one, thus the artificial transition from
     * (RECEIVED, STORED) to (RECOMMENDED) has been enabled.
     */
    createResponseUseCase.createResponse(alertId, RECOMMENDED);
    alertMessageStatusService.transitionAlertMessageStatus(alertId, RECOMMENDED);
  }

}
