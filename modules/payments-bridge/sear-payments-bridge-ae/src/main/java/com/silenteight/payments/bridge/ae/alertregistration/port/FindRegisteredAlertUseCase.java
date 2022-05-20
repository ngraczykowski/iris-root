package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;

import java.util.List;

public interface FindRegisteredAlertUseCase {

  List<RegisteredAlert> find(List<String> registeredAlert);

}
