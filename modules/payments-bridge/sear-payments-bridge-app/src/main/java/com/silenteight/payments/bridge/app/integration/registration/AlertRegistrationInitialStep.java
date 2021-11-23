package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
class AlertRegistrationInitialStep {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final UniversalDataSourceStep universalDataSourceStep;

  void start(UUID alertId) {
    var alertData = alertMessageUseCase.findByAlertMessageId(alertId);
    var alertMessageDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);

    var response = registerAlertUseCase.register(alertData, alertMessageDto);
    log.info("Registered alert {} within ae. AlertName: {}", alertId, response.getAlertName());
    var ctx = new Context(alertData, alertMessageDto, response);
    universalDataSourceStep.invoke(ctx);
  }

}
