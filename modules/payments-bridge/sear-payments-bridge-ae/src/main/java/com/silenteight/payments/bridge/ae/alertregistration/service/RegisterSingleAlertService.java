package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterSingleAlertUseCase;
import com.silenteight.payments.bridge.common.model.AlertMessageModel;
import com.silenteight.payments.bridge.event.AlertDelivered;
import com.silenteight.payments.bridge.event.AlertRegistered;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_DELIVERED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class RegisterSingleAlertService implements RegisterSingleAlertUseCase {

  private final CreateAlertsService createAlertsService;

  @ServiceActivator(inputChannel = ALERT_DELIVERED, outputChannel = ALERT_REGISTERED)
  public AlertRegistered apply(AlertDelivered alertDelivered) {
    AlertMessageModel model = alertDelivered.getAlertModel().orElseThrow();
    var request = RegisterAlertRequest.builder()
        .alertId(model.getId().toString())
        .priority(model.getPriority())
        //  .matchIds()  TODO
        .build();

    var alert = createAlertsService.createAlert(request);

    return new AlertRegistered(
        alert.getAlertId(), alert.getAlertName(), alert.getMatchResponsesAsMap())
        .withAlertModel(model);
  }
}
