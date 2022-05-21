package com.silenteight.hsbc.bridge.alert;

import com.silenteight.hsbc.bridge.report.Alert;

import java.util.Collection;

public interface WarehouseApi {

  void send(Collection<Alert> alerts);
}
