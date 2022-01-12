package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;

import java.util.List;

import static java.util.stream.Collectors.toList;

class RegisterAlertUseCaseMock implements RegisterAlertUseCase {

  @Override
  public RegisterAlertResponse register(RegisterAlertRequest request) {
    return RegisterAlertResponse.builder().build();
  }

  @Override
  public List<RegisterAlertResponse> batchRegistration(
      List<RegisterAlertRequest> registerAlertRequest) {
    return registerAlertRequest.stream().map(a -> RegisterAlertResponse
        .builder()
        .systemId(
            a.getFkcoSystemId())
        .alertName("alerts/" + a.getFkcoSystemId())
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
