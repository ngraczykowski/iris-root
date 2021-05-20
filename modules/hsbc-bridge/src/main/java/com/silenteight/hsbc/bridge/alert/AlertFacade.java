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

  public List<AlertInfo> getAlertByName(@NonNull String name) {
    var alerts = repository.findByName(name).stream().collect(toList());
    return mapToAlertInfo(alerts);
  }

  private List<AlertInfo> mapToAlertInfo(List<AlertEntity> alertEntities) {
    return alertEntities.stream().map(a -> new AlertInfo(a.getId())).collect(toList());
  }

  @Transactional
  public List<AlertComposite> createAndSaveAlerts(@NonNull String bulkId, byte[] bulkPayload) {
    var alertsData = alertPayloadConverter.convert(bulkPayload);
    var processingResult = relationshipProcessor.process(alertsData);

    return processingResult.getProcessedAlerts().stream()
        .map(alert -> saveAlert(bulkId, alert))
        .collect(Collectors.toList());
  }

  private AlertComposite saveAlert(String bulkId, ProcessingResult.ProcessedAlert processedAlert) {
    var alertComposite = AlertComposite.builder()
        .externalId(processedAlert.getExternalId())
        .matches(processedAlert.getMatches());
    var alertEntity =
        new AlertEntity(bulkId, processedAlert.getExternalId(), processedAlert.getDiscriminator());

    processedAlert.getErrorMessage().ifPresent(errorMessage -> {
      alertEntity.error(errorMessage);
      alertComposite.invalid(true);
    });

    repository.save(alertEntity);
    return alertComposite
        .id(alertEntity.getId())
        .build();
  }
}
