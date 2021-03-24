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
  public AlertComposite prepareAndSaveAlert(byte[] payload) {
    var alert = alertMapper.map(payload);
    var alertEntity = getAlertEntity(alert);

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

  private AlertEntity getAlertEntity(Alert alert) {
    var alertRawData = alertRawMapper.map(alert);

    try {
      return alertMapper.map(alertRawData);
    } catch (JsonProcessingException e) {
      throw new AlertPreProcessingFailedException(alertRawData.getCaseId());
    }
  }
}
