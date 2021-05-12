package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.NonNull;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Builder
public class AlertFacade {

  private final AlertPayloadConverter alertPayloadConverter;
  private final AlertRepository repository;
  private final RelationshipProcessor relationshipProcessor;

  //TODO - verify if we really need to store the whole payload.
  // We need either to limit the max number of alert or use direct streaming

  @Transactional
  public List<AlertComposite> createAndSaveAlerts(@NonNull String bulkId, byte[] bulkPayload) {
    var alertsData = alertPayloadConverter.convert(bulkPayload);
    var processingResult = relationshipProcessor.process(alertsData);

    return processingResult.getProcessedAlerts().stream()
        .map(alert -> saveAlert(bulkId, alert))
        .collect(Collectors.toList());
  }

  private AlertComposite saveAlert(String bulkId, ProcessingResult.ProcessedAlert processedAlert) {
    var externalId = processedAlert.getExternalId();
    var alertComposite = AlertComposite.builder()
        .externalId(externalId)
        .matches(processedAlert.getMatches());
    var alertEntity = new AlertEntity(externalId, bulkId);

    processedAlert.getErrorMessage().ifPresent(errorMessage -> {
      alertEntity.error(errorMessage);
      alertComposite.invalid(true);
    });

    repository.save(alertEntity);
    return alertComposite
        .id(alertEntity.getId())
        .build();
  }

  public List<AlertInfo> getAlertByName(String name) {
    return mapToAlertInfo(repository.findByName(name).stream().collect(toList()));
  }

  private List<AlertInfo> mapToAlertInfo(List<AlertEntity> alertEntities) {
    return alertEntities.stream().map(a -> new AlertInfo(a.getId())).collect(toList());
  }
}
