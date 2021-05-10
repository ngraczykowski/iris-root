package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
class AlertUpdater {

  private final AlertRepository repository;

  @Transactional
  void updateNames(@NonNull Map<Long, String> alertIdWithName) {
    alertIdWithName.forEach((k,v) -> {
      var findResult = repository.findById(k);

      findResult.ifPresent(alert -> {
        alert.setName(v);
        alert.setStatus(AlertStatus.PROCESSING);
        repository.save(alert);
      });
    });
  }

  @Transactional
  void updateWithCompletedStatus(@NonNull String name) {
    var result = repository.findByName(name);

    if (result.isEmpty()) {
      log.error("Alert with name: {} has not been found", name);
    } else {
      var alert = result.get();
      alert.setStatus(AlertStatus.COMPLETED);
      repository.save(alert);
    }
  }
}
