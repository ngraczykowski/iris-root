package com.silenteight.payments.bridge.app.integration.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

import java.util.UUID;


@RequiredArgsConstructor
@Getter
class Context {

  private final AlertData alertData;
  private final AlertMessageDto alertMessageDto;
  private final RegisterAlertResponse registerAlertResponse;

  AeAlert getAeAlert() {
    return AeAlert.builder()
        .alertId(alertData.getAlertId())
        .alertName(registerAlertResponse.getAlertName())
        .matches(registerAlertResponse.getMatchResponsesAsMap()).build();
  }

  String getAlertName() {
    return registerAlertResponse.getAlertName();
  }

  UUID getAlertId() {
    return alertData.getAlertId();
  }


}
