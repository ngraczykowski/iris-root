package com.silenteight.payments.bridge.data.retention.port;

import com.silenteight.dataretention.api.v1.AlertsExpired;

public interface SendAlertsExpiredPort {

  void send(AlertsExpired alertsExpired);

}
