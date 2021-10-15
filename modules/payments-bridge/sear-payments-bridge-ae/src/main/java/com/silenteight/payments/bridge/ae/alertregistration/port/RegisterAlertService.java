package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;

import java.util.List;
import java.util.stream.Collectors;

public interface RegisterAlertService {

  RegisterAlertResponse register(RegisterAlertRequest registerAlertRequest);

  default List<RegisterAlertResponse> register(List<RegisterAlertRequest> registerAlertRequest) {
    return registerAlertRequest.stream()
        .map(this::register)
        .collect(Collectors.toList());
  }
}
