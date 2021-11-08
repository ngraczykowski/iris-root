package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.hsbc.bridge.alert.dto.AlertEntityDto;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.report.Alert;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AlertSender {

  private final WarehouseApi warehouseApi;
  private final AgentApi agentApi;
  private final AlertMapper mapper;

  void send(@NonNull Collection<AlertEntityDto> alerts, SendOption[] options) {
    var alertComposites = getAlertInformation(alerts);

    if (ArrayUtils.contains(options, SendOption.AGENTS)) {
      sendToAgents(alertComposites);
    }

    if (ArrayUtils.contains(options, SendOption.WAREHOUSE)) {
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

  private List<AlertDataComposite> getAlertInformation(Collection<AlertEntityDto> alerts) {
    return alerts.stream()
        .map(a -> new AlertDataComposite(a, mapper.toAlertData(a.getPayload())))
        .collect(Collectors.toList());
  }

  private Collection<Alert> getReportAlerts(Collection<AlertDataComposite> alerts) {
    return mapper.toReportAlerts(alerts);
  }

  @Value
  public static class AlertDataComposite {

    AlertEntityDto alertEntity;
    AlertData payload;
  }

  enum SendOption {
    AGENTS,
    WAREHOUSE
  }
}
