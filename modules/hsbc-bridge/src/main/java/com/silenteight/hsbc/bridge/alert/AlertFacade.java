package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;

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

  public List<AlertInfo> getAlertsByIds(Collection<Long> alertIds) {
    return mapToAlertInfo(alertRepository.findByIdIn(alertIds));
  }

  public List<AlertInfo> getAlertByName(String name) {
    return mapToAlertInfo(alertRepository.findByName(name).stream().collect(toList()));
  }

  private List<AlertInfo> mapToAlertInfo(List<AlertEntity> alertEntities) {
    return alertEntities.stream().map(a -> {
      AlertRawData alertRawData = alertRawMapper.map(a.getPayload());

      return new AlertInfo(
          a.getId(),
          a.getCaseId(),
          alertRawData.getCasesWithAlertURL());
    }).collect(toList());
  }

  private AlertEntity prepareAlertEntity(long bulkItemId, Alert alert) {
    var alertRawData = alertRawMapper.map(alert);
    var caseId = alertRawData.getCasesWithAlertURL().getId();

    var payload = alertMapper.map(alertRawData);
    return new AlertEntity(caseId, bulkItemId, payload);
  }
}
