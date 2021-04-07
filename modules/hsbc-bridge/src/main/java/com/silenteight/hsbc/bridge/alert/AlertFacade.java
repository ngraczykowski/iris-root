package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;

import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;

@Builder
public class AlertFacade {

  private final AlertRawMapper alertRawMapper;
  private final AlertRepository alertRepository;

  @Transactional
  public AlertComposite prepareAndSaveAlert(long bulkItemId, byte[] payload) {
    var alertRawData = alertRawMapper.mapBulkPayload(payload);
    var alertEntity = prepareAlertEntity(bulkItemId, alertRawData);

    alertRepository.save(alertEntity);

    return AlertComposite.builder()
        .id(alertEntity.getId())
        .caseId(alertEntity.getCaseId())
        .alertRawData(alertRawData)
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
      AlertRawData alertRawData = alertRawMapper.mapAlertPayload(a.getPayload());

      return new AlertInfo(
          a.getId(),
          a.getCaseId(),
          alertRawData.getFirstCaseWithAlertURL());
    }).collect(toList());
  }

  public String getAlertNameByBulkId(long bulkItemId) {
    return alertRepository.findByBulkItemId(bulkItemId)
        .orElseThrow(() -> new AlertNotFoundException(bulkItemId))
        .getName();
  }

  private AlertEntity prepareAlertEntity(long bulkItemId, AlertRawData alertRawData) {
    var caseId = alertRawData.getFirstCaseWithAlertURL().getId();

    var payload = alertRawMapper.map(alertRawData);
    return new AlertEntity(caseId, bulkItemId, payload);
  }
}
