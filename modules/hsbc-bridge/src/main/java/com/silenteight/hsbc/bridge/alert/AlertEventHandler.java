package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent;
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;
import com.silenteight.hsbc.bridge.bulk.event.UpdateBulkItemStatusEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
class AlertEventHandler {

  private final AlertRepository alertRepository;
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  @Transactional
  public void onUpdateAlertEventWithNameEvent(UpdateAlertWithNameEvent updateAlertWithNameEvent) {
    log.debug("Received updateAlertEventWithNameEvent.");

    updateAlertWithNameEvent.getAlertIdToName()
        .forEach(this::updateAlertAndPublishStatusChangeEvent);
  }

  @EventListener
  public void onAlertRecommendationReadyEvent(AlertRecommendationReadyEvent event) {
    var alertName = event.getAlertName();

    log.debug("Received alertRecommendationReadyEvent, alertName={}", alertName);

    var result = alertRepository.findByName(alertName);

    if (result.isEmpty()) {
      log.error("Alert with name: {} has not been found", alertName);
    } else {
      var bulkItemId = result.get().getBulkItemId();

      publishBulkItemStatusEvent(bulkItemId, BulkStatus.COMPLETED);
    }
  }

  private void updateAlertAndPublishStatusChangeEvent(Long id, String name) {
    var findResult = alertRepository.findById(id);

    findResult.ifPresent(alert -> {
      updateName(name, alert);
      publishBulkItemStatusEvent(alert.getBulkItemId(), BulkStatus.PROCESSING);
    });
  }

  private void publishBulkItemStatusEvent(long bulkItemId, BulkStatus bulkStatus) {
    eventPublisher.publishEvent(UpdateBulkItemStatusEvent
        .builder()
        .bulkItemId(bulkItemId)
        .newStatus(bulkStatus)
        .build());
  }

  private void updateName(String name, AlertEntity alert) {
    alert.setName(name);
    alertRepository.save(alert);
  }
}
