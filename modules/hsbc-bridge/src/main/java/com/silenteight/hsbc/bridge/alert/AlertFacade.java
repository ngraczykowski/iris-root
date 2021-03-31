package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Builder
public class AlertFacade {

  private final AlertMapper alertMapper;
  private final AlertRawMapper alertRawMapper;
  private final AlertRepository alertRepository;

  @Transactional
  public AlertComposite prepareAndSaveAlert(long bulkItemId, byte[] payload) {
    var alert = alertMapper.map(payload);
    var alertEntity = prepareAlertEntity(bulkItemId, alert);

    alertRepository.save(alertEntity);

    return AlertComposite.builder()
        .id(alertEntity.getId())
        .caseId(alertEntity.getCaseId())
        .alert(alert)
        .build();
  }

  public List<AlertInfo> getAlerts(Collection<Long> alertIds) {
    return alertRepository.findByIdIn(alertIds).stream().map(a -> {
      AlertRawData alertRawData = alertRawMapper.map(a.getPayload());

      return new AlertInfo(
          a.getId(),
          a.getCaseId(),
          alertRawData.getCasesWithAlertURL());
    }).collect(Collectors.toList());
  }

  private AlertEntity prepareAlertEntity(long bulkItemId, Alert alert) {
    var alertRawData = alertRawMapper.map(alert);
    var caseId = alertRawData.getCasesWithAlertURL().getId();

    var payload = alertMapper.map(alertRawData);
    return new AlertEntity(caseId, bulkItemId, payload);
  }
}
