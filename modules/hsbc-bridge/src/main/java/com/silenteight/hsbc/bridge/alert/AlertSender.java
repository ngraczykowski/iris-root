package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.dto.AlertEntityDto;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.report.Alert;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class AlertSender {

  private final WarehouseApi warehouseApi;
  private final AgentApi agentApi;
  private final AlertMapper mapper;
  private final AlertRepository alertRepository;

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

  private List<AlertDataComposite> getAlertInformation(Collection<AlertEntityDto> alertEntityDtos) {
    return alertEntityDtos.stream()
        .map(this::createAlertDataComposite)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private Optional<AlertDataComposite> createAlertDataComposite(AlertEntityDto alertEntityDto) {
    try {
      return Optional.of(new AlertDataComposite(
          alertEntityDto,
          mapper.toAlertData(alertEntityDto.getPayload())));
    } catch (AlertConversionException e) {
      log.error("Learning alert data conversion failed, alert name: {}", alertEntityDto.getName());
      alertRepository.findByName(alertEntityDto.getName()).ifPresent(alertEntity -> {
        alertEntity.error(e.getMessage());
        alertRepository.save(alertEntity);
      });
    }
    return Optional.empty();
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
