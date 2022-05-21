package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
class AlertUpdater {

  private final AlertRepository repository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateNames(@NonNull Map<Long, String> alertIdWithName) {
    alertIdWithName.forEach((k, v) -> {
      var findResult = repository.findById(k);

      findResult.ifPresent(alert -> {
        alert.setName(v);
        alert.setStatus(AlertStatus.PROCESSING);
        repository.save(alert);
        log.trace("Alert id:{} stored with name:{}", k, v);
      });
    });
    log.info("Alerts names have been updated. Status: PROCESSING.");
  }

  @Transactional
  public void updateWithRecommendationReadyStatus(@NonNull List<String> names) {
    repository.findByNameIn(names).forEach(alert -> {
      alert.setStatus(AlertStatus.RECOMMENDATION_READY);
      repository.save(alert);
    });
    log.info("Alerts statuses have been updated. Status: RECOMMENDATION_READY.");
  }

  @Transactional
  public void updateWithCompletedStatus(@NonNull List<String> alerts) {
    repository.updateStatusByNames(AlertStatus.COMPLETED, alerts);
    log.info("Alerts statuses have been updated. Status: COMPLETED.");
  }
}
