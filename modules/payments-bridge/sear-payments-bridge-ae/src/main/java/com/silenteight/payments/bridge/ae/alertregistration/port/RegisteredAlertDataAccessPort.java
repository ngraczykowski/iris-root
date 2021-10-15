package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.UUID;

public interface RegisteredAlertDataAccessPort {

  void save(UUID alertId, String alertName);

  String getAlertId(String alertName);
}
