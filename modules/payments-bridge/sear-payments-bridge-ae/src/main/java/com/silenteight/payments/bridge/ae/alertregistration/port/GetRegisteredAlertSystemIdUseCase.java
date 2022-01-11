package com.silenteight.payments.bridge.ae.alertregistration.port;

public interface GetRegisteredAlertSystemIdUseCase {

  String getAlertSystemId(String alertName);
}
