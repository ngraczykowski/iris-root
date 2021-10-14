package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.AlertDeliveredEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.model.CallbackException;
import com.silenteight.payments.bridge.firco.callback.model.NonRecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.model.RecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.port.SendResponseUseCase;
import com.silenteight.payments.common.resource.ResourceName;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
class SendResponseService implements SendResponseUseCase {

  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final CallbackRequestFactory callbackRequestFactory;
  private final ClientRequestDtoMapper mapper;
  private final CommonChannels commonChannels;

  @Override
  public void send(ResponseCompleted responseCompleted) {
    var alertId = ResourceName.create(responseCompleted.getAlert()).getUuid("alerts");
    var status = AlertMessageStatus.valueOf(
        ResourceName.create(responseCompleted.getStatus()).get("status"));

    var alert = alertMessageUseCase.findByAlertMessageId(alertId);
    var alertDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);
    var requestDto = mapper.mapToAlertDecision(alert, alertDto, status);

    try {
      log.info("Calling callback. Alert: [{}], status: [{}]", alertId, status);
      callbackRequestFactory.create(requestDto).invoke();
      log.info("The callback invoked successfully. Alert: [{}], status: [{}]", alertId, status);

      commonChannels.deliveredAlertChannel().send(
          MessageBuilder.withPayload(new AlertDeliveredEvent(alertId, status.name())).build());

    } catch (NonRecoverableCallbackException exception) {
      log.info("The callback failed with non-recoverable exception. "
          + "Alert: [{}], status: [{}]", alertId, status);
      enrichAlert(exception, alertId, status);
      throw exception;

    } catch (RecoverableCallbackException exception) {
      log.info("The callback failed with recoverable exception. "
          + "Alert: [{}], status: [{}]", alertId, status);
      enrichAlert(exception, alertId, status);
      throw exception;
    }
  }

  private void enrichAlert(CallbackException exception, UUID alertId, AlertMessageStatus status) {
    exception.setStatus(status);
    exception.setAlertId(alertId);
  }
}
