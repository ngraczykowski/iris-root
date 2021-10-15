package com.silenteight.payments.bridge.ae.alertregistration.port;

public interface GetRegisteredAlertIdUseCase {

  String getAlertId(String alertName);
}
