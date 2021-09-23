package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;

import java.util.List;

public interface RegisterAlertUseCase {

  List<RegisterAlertResponse> register(List<RegisterAlertRequest> registerAlertRequest);
}
