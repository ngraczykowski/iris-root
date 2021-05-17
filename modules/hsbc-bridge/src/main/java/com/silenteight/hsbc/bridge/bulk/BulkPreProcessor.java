package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PRE_PROCESSED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.STORED;
import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Slf4j
class BulkPreProcessor {

  private final AlertFacade alertFacade;
  private final BulkRepository bulkRepository;
  private final MatchFacade matchFacade;

  @Scheduled(fixedDelay = 15 * 1000, initialDelay = 2000)
  @Transactional
  public void processStoredBulks() {
    bulkRepository.findByStatus(STORED).forEach(this::tryToPreProcessBulk);
  }

  private void tryToPreProcessBulk(Bulk bulk) {
    try {
      var alertMatchIds = preProcessBulk(bulk);

      if (alertMatchIds.isEmpty()) {
        log.warn("Bulk: {} does not contain valid alerts", bulk.getId());
        bulk.error("No valid alerts to be processed!");
      } else {
        bulk.setStatus(PRE_PROCESSED);
      }

      log.debug("Bulk pre-processing finished, bulkId={}", bulk.getId());
    } catch (RuntimeException ex) {
      log.error("Bulk pre-processing failed, bulkId={}", bulk.getId(), ex);
      bulk.error("Bulk pre-processing failed due to: " + ex.getMessage());
    }
  }

  private List<AlertMatchIdComposite> preProcessBulk(Bulk bulk) {
    var payloadEntity = bulk.getPayload();
    var alerts = alertFacade.createAndSaveAlerts(bulk.getId(), payloadEntity.getPayload());

    return alerts.stream()
        .filter(not(AlertComposite::isInvalid))
        .map(this::saveAlertMatches)
        .collect(Collectors.toList());
  }

  private AlertMatchIdComposite saveAlertMatches(AlertComposite alertComposite) {
    var matchIds =
        matchFacade.prepareAndSaveMatches(alertComposite.getId(), alertComposite.getMatches());

    return AlertMatchIdComposite.builder()
        .alertInternalId(alertComposite.getId())
        .alertExternalId(alertComposite.getExternalId())
        .matchIds(matchIds)
        .build();
  }
}
