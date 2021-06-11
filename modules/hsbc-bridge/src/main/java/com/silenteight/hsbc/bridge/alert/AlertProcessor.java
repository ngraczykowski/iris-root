package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.hsbc.bridge.alert.AlertMetadata.MetadataKey.EXTENDED_ATTRIBUTE_5;
import static com.silenteight.hsbc.bridge.alert.AlertMetadata.MetadataKey.TRACKING_ID;
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
    alert.setDiscriminator(alertData.getFlagKey());
    alert.setExternalId(alertData.getId());
    fillMetadata(alert.getMetadata(), alertData.getCaseInformation());
  }

  private void fillMetadata(List<AlertMetadata> metadata, CaseInformation caseInformation) {
    metadata.add(new AlertMetadata(EXTENDED_ATTRIBUTE_5, caseInformation.getExtendedAttribute5()));
    metadata.add(new AlertMetadata(TRACKING_ID, caseInformation.getFlagKey()));
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
