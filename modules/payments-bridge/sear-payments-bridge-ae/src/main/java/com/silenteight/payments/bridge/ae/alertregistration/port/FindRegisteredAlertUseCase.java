package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;

import java.util.List;

public interface FindRegisteredAlertUseCase {

  List<RegisteredAlert> find(List<FindRegisteredAlertRequest> registeredAlert);

}
