package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.dataretention.api.v1.AlertsExpired;

public interface AlertsExpiredClientGateway {

  void indexRequest(AlertsExpired alertsExpired);
}
