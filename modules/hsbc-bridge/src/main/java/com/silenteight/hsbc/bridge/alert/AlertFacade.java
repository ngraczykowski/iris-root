package com.silenteight.hsbc.bridge.alert;

import lombok.*;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import com.fasterxml.jackson.core.JsonProcessingException;

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
        .alert(alert)
        .build();
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
