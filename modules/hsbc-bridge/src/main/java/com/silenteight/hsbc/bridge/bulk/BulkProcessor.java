package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchIdComposite;
import com.silenteight.hsbc.bridge.report.WarehouseFacade;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PRE_PROCESSED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
class BulkProcessor {

  private final AdjudicationFacade adjudicationFacade;
  private final WarehouseFacade warehouseFacade;
  private final BulkRepository bulkRepository;

  @Scheduled(fixedDelay = 15 * 1000, initialDelay = 2000)
  @Transactional
  public void processPreProcessedBulks() {
    bulkRepository.findByStatus(PRE_PROCESSED).forEach(this::tryToProcessBulk);
  }

  private void tryToProcessBulk(Bulk bulk) {
    log.info("Processing bulk: {}", bulk.getId());

    try {
      if (bulk.isLearning()) {
        processLearningBulk(bulk);
      } else {
        processSolvingBulk(bulk);
      }
    } catch (Exception exception) {
      log.error("Bulk processing failed!", exception);
      bulk.error("Bulk processing failed due to: " + exception.getMessage());
    }

    bulkRepository.save(bulk);
  }

  private void processSolvingBulk(Bulk bulk) {
    var compositeById = bulk.getValidAlerts()
        .stream()
        .collect(toMap(BulkAlertEntity::getExternalId, BulkProcessor::toComposite));

    var analysisId = adjudicationFacade.registerAlertWithMatchesAndAnalysis(compositeById);

    bulk.setAnalysisId(analysisId);

    bulk.setStatus(PROCESSING);
  }

  private void processLearningBulk(Bulk bulk) {
    var alerts = bulk.getValidAlerts();
    var compositeById = alerts.stream()
        .collect(toMap(BulkAlertEntity::getExternalId, BulkProcessor::toComposite));

    adjudicationFacade.registerAlertWithMatches(compositeById);
    sendToWarehouse(alerts);

    bulk.setStatus(COMPLETED);
  }

  private static AlertMatchIdComposite toComposite(BulkAlertEntity alert) {
    return AlertMatchIdComposite.builder()
        .alertExternalId(alert.getExternalId())
        .alertInternalId(alert.getId())
        .matchIds(getMatchIds(alert.getMatches()))
        .build();
  }

  private void sendToWarehouse(Collection<BulkAlertEntity> alertEntities) {
    var alertIds = alertEntities.stream()
        .map(BulkAlertEntity::getId)
        .collect(Collectors.toSet());

    warehouseFacade.findAndSendAlerts(alertIds);
  }

  private static Collection<MatchIdComposite> getMatchIds(
      Collection<BulkAlertMatchEntity> matches) {
    return matches.stream()
        .map(m -> new MatchIdComposite(m.getId(), m.getExternalId()))
        .collect(toList());
  }
}
