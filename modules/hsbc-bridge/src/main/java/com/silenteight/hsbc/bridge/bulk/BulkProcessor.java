package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertComposite;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkPreProcessingFinishedEvent;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.STORED;

@RequiredArgsConstructor
@Slf4j
class BulkProcessor {

  private final AlertFacade alertFacade;
  private final BulkRepository bulkRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final MatchFacade matchFacade;

  @Scheduled(fixedDelay = 15 * 1000, initialDelay = 2000)
  @Transactional
  public void processStoredBulks() {
    bulkRepository.findByStatus(STORED).forEach(this::processBulk);
  }

  private void processBulk(Bulk bulk) {
    var bulkId = bulk.getId();

    try {
      var alertMatchIds = processItems(bulk);
      bulk.setStatus(BulkStatus.PRE_PROCESSED);

      log.debug("Bulk processing finished, bulkId={}", bulkId);
      publishPreProcessingFinishedEvent(bulkId, alertMatchIds);
    } catch (RuntimeException ex) {
      log.debug("Bulk processing failed, bulkId={}", bulkId, ex);
      updateBulkWithError(bulk, ex.getMessage());
    }
  }

  private List<AlertMatchIdComposite> processItems(Bulk bulk) {
    var payloadEntity = bulk.getPayload();
    var alerts = alertFacade.createAndSaveAlerts(bulk.getId(), payloadEntity.getPayload());

    return alerts.stream()
        .map(this::saveAlertMatches)
        .collect(Collectors.toList());
  }

  private void publishPreProcessingFinishedEvent(String id, List<AlertMatchIdComposite> alerts) {
    eventPublisher.publishEvent(BulkPreProcessingFinishedEvent.builder()
        .bulkId(id)
        .alertMatchIdComposites(alerts)
        .build());
  }

  private void updateBulkWithError(Bulk bulk, String message) {
    bulk.error(message);
    bulkRepository.save(bulk);
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
