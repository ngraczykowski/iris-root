package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;

import java.util.List;
import java.util.UUID;

public interface RegisteredAlertDataAccessPort {

  void save(SaveRegisteredAlertRequest request);

  String getAlertId(String alertName);

  List<UUID> delete(List<String> alertNames);
}
