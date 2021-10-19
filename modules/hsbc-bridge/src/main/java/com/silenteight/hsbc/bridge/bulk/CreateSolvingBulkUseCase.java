package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
class CreateSolvingBulkUseCase {

  private static final String PREFIX = "re-recommend/";
  private final BulkRepository repository;
  private final AlertFacade alertFacade;
  private final ApplicationEventPublisher eventPublisher;

  public String createBulkWithAlerts(List<String> alerts) {
    var bulkId = generateBulkId();
    repository.save(new Bulk(bulkId, false));
    createAlerts(bulkId, alerts);

    return bulkId;
  }

  private void createAlerts(String bulkId, List<String> alerts) {
    try {
      log.info("Start re-processing alerts, batchId: {}", bulkId);
      alertFacade.reProcessAlerts(bulkId, alerts);
      eventPublisher.publishEvent(new BulkStoredEvent(bulkId));
    } catch (Exception e) {
      log.error("Cannot create alerts, batchId = {}", bulkId, e);
      repository.findById(bulkId).ifPresent(bulk -> {
        bulk.error("Unable to create alerts, due to: " + e.getMessage());
        repository.save(bulk);
      });
    }
  }

  private String generateBulkId() {
    return PREFIX + UUID.randomUUID();
  }
}
