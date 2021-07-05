package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.report.Alert;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AlertGetter {

  private final AlertMapper mapper;
  private final AlertRepository repository;

  public List<AlertInformation> getAlertInformation(Collection<Long> ids) {
    var entities = repository.findByIdIn(ids);
    return entities.stream()
        .map(e -> new AlertInformation(e, mapper.toAlertData(e.getPayload().getPayload())))
        .collect(Collectors.toList());
  }

  public Collection<Alert> getReportAlerts(Collection<AlertInformation> alerts) {
    return mapper.toReportAlerts(alerts);
  }

  @Value
  public static class AlertInformation {

    AlertEntity alertEntity;
    AlertData payload;
  }
}
