package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertData;

import java.util.List;

import static java.util.stream.Collectors.toList;

class RegisterAlertUseCaseMock implements RegisterAlertUseCase {

  @Override
  public RegisterAlertResponse register(
      AlertData alertData,
      AlertMessageDto alertMessageDto) {
    return RegisterAlertResponse.builder().build();
  }

  @Override
  public List<RegisterAlertResponse> batchRegistration(
      List<RegisterAlertRequest> registerAlertRequest) {
    return registerAlertRequest.stream().map(a -> RegisterAlertResponse
        .builder()
        .alertId(
            a.getAlertId())
        .alertName("alerts/" + a.getAlertId())
        .matchResponses(
            a.getMatchIds()
                .stream()
                .map(m -> RegisterMatchResponse
                    .builder()
                    .matchId(m)
                    .matchName("matches/" + m)
                    .build())
                .collect(
                    toList()))
        .build()).collect(toList());
  }
}
