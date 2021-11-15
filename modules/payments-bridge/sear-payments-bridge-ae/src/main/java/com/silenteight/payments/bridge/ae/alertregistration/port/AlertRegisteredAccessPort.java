package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlertWithMatches;

import java.util.List;
import java.util.UUID;

public interface AlertRegisteredAccessPort {

  List<RegisteredAlertWithMatches> findRegistered(List<UUID> registrations);

}
