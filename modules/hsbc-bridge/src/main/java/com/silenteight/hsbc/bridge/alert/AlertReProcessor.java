package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Slf4j
class AlertReProcessor {

  private final AlertRepository repository;
  private final EntityManager entityManager;

  @Transactional
  public void reProcessAlerts(String bulkId, List<String> alertsName) {
    log.info("Creating copy of raw alerts, batchId: {}", bulkId);
    repository.findByNameIn(alertsName)
        .forEach(e -> createCopyOfAlertWithPayload(bulkId, e.getPayload()));
  }

  private void createCopyOfAlertWithPayload(String bulkId, AlertDataPayloadEntity payloadEntity) {
    var alertEntity = new AlertEntity(bulkId);
    alertEntity.setPayload(payloadEntity);

    repository.save(alertEntity);
    entityManager.flush();
    entityManager.clear();
  }
}
