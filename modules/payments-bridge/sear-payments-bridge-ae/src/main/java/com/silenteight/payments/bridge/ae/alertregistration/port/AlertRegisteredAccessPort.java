package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;

import java.util.List;

public interface AlertRegisteredAccessPort {

  List<RegisteredAlert> findRegistered(List<String> registeredAlert);

}
