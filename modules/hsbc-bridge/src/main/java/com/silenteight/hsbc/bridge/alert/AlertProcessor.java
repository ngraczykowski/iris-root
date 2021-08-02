package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static com.silenteight.hsbc.bridge.alert.AlertStatus.PRE_PROCESSED;
import static com.silenteight.hsbc.bridge.alert.AlertStatus.STORED;

@RequiredArgsConstructor
@Slf4j
class AlertProcessor {

  private final AlertPayloadConverter payloadConverter;
  private final AlertRepository repository;
  private final RelationshipProcessor relationshipProcessor;
  private final MatchFacade matchFacade;

  @Transactional
  public void preProcessAlertsWithinBulk(@NonNull String bulkId) {
    try (var alerts = repository.findByBulkIdAndStatus(bulkId, STORED)) {
      alerts.forEach(this::tryToPreProcessAlert);
    }

    log.debug("Alerts pre-processing have been completed, batchId = {}", bulkId);
  }

  private void tryToPreProcessAlert(AlertEntity alert) {
    try {
      preProcessAlert(alert);
    } catch (RuntimeException exception) {
      log.error("Alert pre-processing failure, id = {}", alert.getId(), exception);
      alert.error("Alert id pre-processing failure");
    }
  }

  private void preProcessAlert(AlertEntity alert) {
    var alertData = payloadConverter.convertAlertData(alert.getPayloadAsBytes());

    fillAlert(alert, alertData);
    tryToProcessRelationshipsAndCreateMatches(alert, alertData);
  }

  private void fillAlert(AlertEntity alert, AlertData alertData) {
    alert.setAlertTime(getAlertTime(alertData.getCaseInformation()));
    alert.setDiscriminator(alertData.getFlagKey());
    alert.setExternalId(alertData.getId());
    alert.getMetadata().addAll(new AlertMetadataCollector().collectFromAlertData(alertData));
  }

  private static OffsetDateTime getAlertTime(CaseInformation caseInformation) {
    return caseInformation.getAlertTime().orElse(OffsetDateTime.now());
  }

  private void tryToProcessRelationshipsAndCreateMatches(AlertEntity alert, AlertData alertData) {
    try {
      var processedAlert = relationshipProcessor.process(alertData);
      matchFacade.prepareAndSaveMatches(alert.getId(), processedAlert.getMatches());
      alert.setStatus(PRE_PROCESSED);
    } catch (InvalidAlertDataException exception) {
      alert.error(exception.getMessage());
    }
  }
}
