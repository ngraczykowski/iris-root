package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.ResourceName;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.model.CallbackException;
import com.silenteight.payments.bridge.firco.callback.model.NonRecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.model.RecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.port.AlertDeliveredPublisherPort;
import com.silenteight.payments.bridge.firco.callback.port.SendResponseUseCase;
import com.silenteight.payments.bridge.firco.dto.output.ClientRequestDto;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationId;
import com.silenteight.payments.bridge.firco.recommendation.port.GetRecommendationUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;
import com.silenteight.sep.base.aspects.logging.LogContext;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
class SendResponseService implements SendResponseUseCase {

  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final GetRecommendationUseCase getRecommendationUseCase;
  private final CallbackRequestFactory callbackRequestFactory;
  private final ClientRequestDtoMapper mapper;
  private final AlertDeliveredPublisherPort alertDeliveredPublisherPort;

  @LogContext
  @Override
  @Timed(value = "pb.firco.use_cases", extraTags = { "package", "callback" })
  public void send(ResponseCompleted responseCompleted) {
    var alertId = ResourceName.create(responseCompleted.getAlert()).getUuid("alerts");
    MDC.put("alertId", alertId.toString());

    var alert = alertMessageUseCase.findByAlertMessageId(alertId);
    var alertDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);

    var status = AlertMessageStatus.valueOf(
        ResourceName.create(responseCompleted.getStatus()).get("status"));
    MDC.put("status", status.toString());

    var recommendation = getRecommendationUseCase.get(
        RecommendationId.fromName(responseCompleted.getRecommendation()));
    MDC.put("recommendation", recommendation.getAction());

    var requestDto = mapper.mapToAlertDecision(alert, alertDto, recommendation);

    sendDecision(alertId, status, requestDto, recommendation.getAction());
  }

  private void sendDecision(
      UUID alertId, AlertMessageStatus status, ClientRequestDto requestDto, String action) {

    try {
      log.info("Executing the callback for alert [{}]. Recommendation: [{}]", alertId, action);
      callbackRequestFactory.create(requestDto).execute();
      log.info("The callback invoked successfully.");
      alertDeliveredPublisherPort.sendDelivered(alertId, status);

    } catch (NonRecoverableCallbackException exception) {
      log.error("The callback failed with non-recoverable exception.");
      enrichException(exception, alertId, status);
      throw exception;
    } catch (RecoverableCallbackException exception) {
      log.warn("The callback failed with recoverable exception.");
      enrichException(exception, alertId, status);
      throw exception;
    }
  }

  private static void enrichException(
      CallbackException exception, UUID alertId, AlertMessageStatus status) {

    exception.setStatus(status);
    exception.setAlertId(alertId);
  }
}
