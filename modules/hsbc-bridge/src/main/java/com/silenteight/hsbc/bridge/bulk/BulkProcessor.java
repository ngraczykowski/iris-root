package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.alert.AlertEntity;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.alert.AlertStatus;
import com.silenteight.hsbc.bridge.alert.LearningAlertProcessor;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchIdComposite;

import io.micrometer.core.annotation.Timed;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
class BulkProcessor {

  private final AdjudicationFacade adjudicationFacade;
  private final AlertFacade alertFacade;
  private final LearningAlertProcessor learningAlertProcessor;
  private final BulkRepository bulkRepository;

  @Transactional
  @Timed(value = "bulk_processor_try_to_process_solving_bulk", histogram = true)
  public void tryToProcessSolvingBulk() {
    bulkRepository.findFirstByStatusOrderByCreatedAtAsc(BulkStatus.PRE_PROCESSING).ifPresent(bulk -> {
      log.debug("Pre_Processing solving batch taken to process id: {}", bulk.getId());
      try {
        processSolvingBulk(bulk);
      } catch (Exception exception) {
        log.error("Solving batch processing failed!", exception);
        bulk.error("Solving batch processing failed due to: " + exception.getMessage());
      }
      bulkRepository.save(bulk);
    });
  }

  @Transactional
  @Timed(value = "bulk_processor_try_to_process_learning_bulk",histogram = true)
  public void tryToProcessLearningBulk() {
    bulkRepository.findFirstByStatusOrderByCreatedAtAsc(BulkStatus.PRE_PROCESSING).ifPresent(bulk -> {
      log.debug("Pre_Processing learning batch taken to process id: {}", bulk.getId());
      try {
        processLearningBulk(bulk);
      } catch (Exception exception) {
        log.error("Learning batch processing failed!", exception);
        bulk.error("Learning batch processing failed due to: " + exception.getMessage());
      }
      bulkRepository.save(bulk);
    });
  }

  private void processSolvingBulk(Bulk bulk) {
    var compositeById = bulk.getValidAlerts()
        .stream()
        .collect(Collectors.toMap(BulkAlertEntity::getExternalId, BulkProcessor::toComposite, (e, n) -> e));

    var analysisId = adjudicationFacade.registerAlertWithMatchesAndAnalysis(compositeById);

    bulk.setAnalysisId(analysisId);
    bulk.setStatus(BulkStatus.PROCESSING);
    log.info("Batch sent to AE: {}. Status set to PROCESSING", bulk.getId());
  }

  private void processLearningBulk(Bulk bulk) {
    var alerts = bulk.getValidAlerts();

    var registeredAlerts =
        alertFacade.getRegisteredAlerts(toIdsWithDiscriminators(alerts));

    var unregisteredAlerts =
        getUnregisteredAlerts(alerts, registeredAlerts);

    log.info(
        "Picked up learning batch id: {}, with size of: {} registeredAlerts and {} unregisteredAlerts",
        bulk.getId(),
        registeredAlerts.size(),
        unregisteredAlerts.size());

    register(unregisteredAlerts);

    processLearningAlerts(alerts, unregisteredAlerts);

    bulk.setStatus(BulkStatus.COMPLETED);
    log.info("Learning batch {} has been completed. Status set to COMPLETED", bulk.getId());
  }

  private void register(Collection<BulkAlertEntity> unregisteredAlerts) {
    if (!unregisteredAlerts.isEmpty()) {
      log.info("Start registering {} alerts", unregisteredAlerts.size());

      var compositeById = unregisteredAlerts.stream()
          .collect(Collectors.toMap(BulkAlertEntity::getExternalId, BulkProcessor::toComposite));

      adjudicationFacade.registerAlertWithMatches(compositeById);
    }
  }

  private Collection<BulkAlertEntity> getUnregisteredAlerts(
      Collection<BulkAlertEntity> alerts, Collection<AlertEntity> registeredAlerts) {
    return alerts.stream()
        .filter(alert -> registeredAlerts.stream()
            .noneMatch(registeredAlert ->
                concatIdWithDiscriminator(
                    registeredAlert.getExternalId(), registeredAlert.getDiscriminator())
                    .equals(
                        concatIdWithDiscriminator(
                            alert.getExternalId(), alert.getDiscriminator()))))
        .collect(Collectors.toList());
  }

  private List<Long> getAlreadyRegisteredAlertIdsFromRequest(
      Collection<BulkAlertEntity> alerts, Collection<BulkAlertEntity> unregisteredAlerts) {
    return alerts.stream()
        .filter(alert -> unregisteredAlerts.stream()
            .noneMatch(unregisteredAlert ->
                concatIdWithDiscriminator(
                    unregisteredAlert.getExternalId(), unregisteredAlert.getDiscriminator())
                    .equals(
                        concatIdWithDiscriminator(
                            alert.getExternalId(), alert.getDiscriminator()))))
        .filter(alert -> alert.getStatus() != AlertStatus.ERROR)
        .map(BulkAlertEntity::getId)
        .collect(Collectors.toList());
  }

  private Stream<String> toIdsWithDiscriminators(Collection<BulkAlertEntity> alerts) {
    return alerts.stream()
        .map(alert -> concatIdWithDiscriminator(alert.getExternalId(), alert.getDiscriminator()));
  }

  private String concatIdWithDiscriminator(String externalId, String discriminator) {
    return externalId + "_" + discriminator;
  }

  private static AlertMatchIdComposite toComposite(BulkAlertEntity alert) {
    return AlertMatchIdComposite.builder()
        .alertExternalId(alert.getExternalId())
        .alertInternalId(alert.getId())
        .alertTime(alert.getAlertTime())
        .matchIds(getMatchIds(alert.getMatches()))
        .build();
  }

  private void processLearningAlerts(
      Collection<BulkAlertEntity> alerts,
      Collection<BulkAlertEntity> unregisteredAlerts) {

    var registeredIds =
        getAlreadyRegisteredAlertIdsFromRequest(alerts, unregisteredAlerts);

    var unregisteredIds =
        unregisteredAlerts.stream().map(BulkAlertEntity::getId).collect(Collectors.toList());

    learningAlertProcessor.process(registeredIds, unregisteredIds);
  }

  private static Collection<MatchIdComposite> getMatchIds(
      Collection<BulkAlertMatchEntity> matches) {
    return matches.stream()
        .map(m -> new MatchIdComposite(m.getId(), m.getExternalId()))
        .collect(Collectors.toList());
  }
}
