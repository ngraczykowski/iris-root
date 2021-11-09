package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;

public interface RegisteredAlertDataAccessPort {

  void save(SaveRegisteredAlertRequest request);

  String getAlertId(String alertName);
}
