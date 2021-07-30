package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.alert.LearningAlertProcessor;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchIdComposite;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
  private final LearningAlertProcessor learningAlertProcessor;
  private final BulkRepository bulkRepository;

  @Scheduled(fixedDelay = 2 * 1000, initialDelay = 2000)
  @SchedulerLock(name = "processPreProcessedBulks", lockAtLeastFor = "PT1S", lockAtMostFor = "PT2M")
  @Transactional
  public void processPreProcessedBulks() {
    LockAssert.assertLocked();

    bulkRepository.findFirstByStatusOrderByCreatedAtAsc(PRE_PROCESSED)
        .ifPresent(this::tryToProcessBulk);
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
        .collect(toMap(BulkAlertEntity::getExternalId, BulkProcessor::toComposite, (e, n) -> e));

    var analysisId = adjudicationFacade.registerAlertWithMatchesAndAnalysis(compositeById);

    bulk.setAnalysisId(analysisId);
    bulk.setStatus(PROCESSING);
  }

  private void processLearningBulk(Bulk bulk) {
    var alerts = bulk.getValidAlerts();
    var compositeById = alerts.stream()
        .collect(toMap(BulkAlertEntity::getExternalId, BulkProcessor::toComposite));

    adjudicationFacade.registerAlertWithMatches(compositeById);
    processLearningAlerts(alerts);

    bulk.setStatus(COMPLETED);
  }

  private static AlertMatchIdComposite toComposite(BulkAlertEntity alert) {
    return AlertMatchIdComposite.builder()
        .alertExternalId(alert.getExternalId())
        .alertInternalId(alert.getId())
        .matchIds(getMatchIds(alert.getMatches()))
        .build();
  }

  private void processLearningAlerts(Collection<BulkAlertEntity> alertEntities) {
    var alertIds = alertEntities.stream()
        .map(BulkAlertEntity::getId)
        .collect(Collectors.toSet());

    learningAlertProcessor.process(alertIds);
  }

  private static Collection<MatchIdComposite> getMatchIds(
      Collection<BulkAlertMatchEntity> matches) {
    return matches.stream()
        .map(m -> new MatchIdComposite(m.getId(), m.getExternalId()))
        .collect(toList());
  }
}
