package com.silenteight.payments.bridge.data.retention;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.payments.bridge.data.retention.port.SendAlertsExpiredPort;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;

public class SendAlertExpiredPortMock implements SendAlertsExpiredPort {

  private final List<AlertsExpired> alertsExpiredList = new LinkedList<>();

  @Override
  public void send(@Nonnull AlertsExpired alertsExpired) {
    alertsExpiredList.add(alertsExpired);
  }

  public int getAlertsExpiredCount() {
    return (int) alertsExpiredList
        .stream()
        .map(AlertsExpired::getAlertsList)
        .mapToLong(List::size)
        .sum();
  }
}
