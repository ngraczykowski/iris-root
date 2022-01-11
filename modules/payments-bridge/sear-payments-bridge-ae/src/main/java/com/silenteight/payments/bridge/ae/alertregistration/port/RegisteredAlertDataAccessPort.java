package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;

import java.util.List;

public interface RegisteredAlertDataAccessPort {

  void save(List<SaveRegisteredAlertRequest>  request);

  String getAlertSystemId(String alertName);

  List<String> delete(List<String> alertNames);
}
