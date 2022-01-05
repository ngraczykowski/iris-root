package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister;
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts;

public interface AlertRegistrationService {

  RegisteredAlerts registerAlerts(AlertsToRegister alertsToRegister);

}
