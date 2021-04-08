package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkPreProcessingFinishedEvent;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.bulk.repository.BulkQueryRepository;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class BulkProcessor {

  private final AlertFacade alertFacade;
  private final BulkQueryRepository bulkQueryRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final MatchFacade matchFacade;

  @EventListener
  @Async
  public void onBulkStoredEvent(BulkStoredEvent bulkStoredEvent) {
    var bulkId = bulkStoredEvent.getBulkId();
    log.info("Received bulkStoredEvent, bulkId: {}", bulkId);

    var alertMatchIds = processBulk(bulkId);
    log.info("Bulk processing finished, bulkId={}", bulkId);
    eventPublisher.publishEvent(new BulkPreProcessingFinishedEvent(alertMatchIds));
  }

  @Transactional
  public List<AlertMatchIdComposite> processBulk(@NonNull String bulkId) {
    log.info("Bulk processing started, bulkId={}", bulkId);

    var bulk = bulkQueryRepository.findById(bulkId);
    return bulk.getItems().stream()
        .map(this::saveAndCollectAlertAndMatches)
        .collect(Collectors.toList());
  }

  private AlertMatchIdComposite saveAndCollectAlertAndMatches(BulkItem bulkItem) {
    var alertComposite = alertFacade.prepareAndSaveAlert(bulkItem.getId(), bulkItem.getPayload());
    var matchIds = matchFacade.prepareAndSaveMatches(alertComposite);

    return AlertMatchIdComposite.builder()
        .alertId(alertComposite.getId())
        .caseId(alertComposite.getCaseId())
        .matchIds(matchIds)
        .build();
  }
}
