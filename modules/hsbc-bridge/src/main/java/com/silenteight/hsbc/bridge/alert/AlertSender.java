package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.report.Alert;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.alert.AlertSender.SendOption.AGENTS;
import static com.silenteight.hsbc.bridge.alert.AlertSender.SendOption.WAREHOUSE;
import static org.apache.commons.lang3.ArrayUtils.contains;

@RequiredArgsConstructor
class AlertSender {

  private final WarehouseApi warehouseApi;
  private final AgentApi agentApi;
  private final AlertMapper mapper;

  void send(@NonNull Collection<AlertEntity> alerts, SendOption[] options) {
    var alertComposites = getAlertInformation(alerts);

    if (contains(options, AGENTS)) {
      sendToAgents(alertComposites);
    }

    if (contains(options, WAREHOUSE)) {
      sendToWarehouse(alertComposites);
    }
  }

  private void sendToAgents(Collection<AlertDataComposite> alerts) {
    var alertsData = alerts.stream()
        .map(AlertDataComposite::getPayload)
        .collect(Collectors.toList());

    agentApi.sendIsPep(alertsData);
    agentApi.sendHistorical(alertsData);
  }

  private void sendToWarehouse(Collection<AlertDataComposite> alerts) {
    var reportAlerts = getReportAlerts(alerts);
    warehouseApi.send(reportAlerts);
  }

  private List<AlertDataComposite> getAlertInformation(Collection<AlertEntity> alerts) {
    return alerts.stream()
        .map(e -> new AlertDataComposite(e, mapper.toAlertData(e.getPayload().getPayload())))
        .collect(Collectors.toList());
  }

  private Collection<Alert> getReportAlerts(Collection<AlertDataComposite> alerts) {
    return mapper.toReportAlerts(alerts);
  }

  @Value
  public static class AlertDataComposite {

    AlertEntity alertEntity;
    AlertData payload;
  }

  enum SendOption {
    AGENTS,
    WAREHOUSE
  }
}
