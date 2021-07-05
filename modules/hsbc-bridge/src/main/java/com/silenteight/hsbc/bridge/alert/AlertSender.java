package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertGetter.AlertInformation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.alert.AlertSender.SendOption.IS_PEP;
import static com.silenteight.hsbc.bridge.alert.AlertSender.SendOption.WAREHOUSE;
import static org.apache.commons.lang3.ArrayUtils.contains;

@RequiredArgsConstructor
public class AlertSender {

  private final WarehouseApi warehouseApi;
  private final IsPepApi isPepApi;
  private final AlertGetter alertGetter;

  public void send(Set<Long> ids, SendOption[] options) {
    var alerts = getAlerts(ids);

    if (contains(options, IS_PEP)) {
      sendToIsPep(alerts);
    }

    if (contains(options, WAREHOUSE)) {
      sendToWarehouse(alerts);
    }
  }

  private void sendToIsPep(Collection<AlertInformation> alerts) {
    var alertsData = alerts.stream()
        .map(AlertInformation::getPayload)
        .collect(Collectors.toList());

    isPepApi.send(alertsData);
  }

  private void sendToWarehouse(Collection<AlertInformation> alerts) {
    var reportAlerts = alertGetter.getReportAlerts(alerts);
    warehouseApi.send(reportAlerts);
  }

  private List<AlertInformation> getAlerts(Collection<Long> ids) {
    return alertGetter.getAlertInformation(ids);
  }

  public enum SendOption {
    IS_PEP,
    WAREHOUSE
  }
}
