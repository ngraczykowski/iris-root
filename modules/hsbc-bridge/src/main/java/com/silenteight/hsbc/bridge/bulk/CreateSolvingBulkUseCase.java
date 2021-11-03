package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.bulk.rest.AlertIdWrapper;
import com.silenteight.hsbc.bridge.bulk.rest.AlertReRecommend;

import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class CreateSolvingBulkUseCase {

  private static final String PREFIX = "reRecommend-";
  private static final boolean IS_LEARNING_BULK = false;
  private final BulkRepository repository;
  private final AlertFacade alertFacade;
  private final ApplicationEventPublisher eventPublisher;

  public String createBulkWithAlerts(AlertReRecommend alertReRecommend) {
    var bulkId = generateBulkId();
    repository.save(new Bulk(bulkId, IS_LEARNING_BULK));

    var alertNames = alertReRecommend.getAlerts().stream()
        .map(AlertIdWrapper::getAlertId)
        .collect(Collectors.toList());
    createAlerts(bulkId, alertNames);
    return bulkId;
  }

  private void createAlerts(String bulkId, List<String> alerts) {
    try {
      log.info("Start re-processing alerts, batchId: {}", bulkId);
      alertFacade.reProcessAlerts(bulkId, alerts);
      eventPublisher.publishEvent(new BulkStoredEvent(bulkId, IS_LEARNING_BULK));
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
