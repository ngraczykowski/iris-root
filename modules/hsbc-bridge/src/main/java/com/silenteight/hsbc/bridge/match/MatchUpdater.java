package com.silenteight.hsbc.bridge.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertStatus;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
class MatchUpdater {

  private final MatchRepository repository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateNames(@NonNull Map<Long, String> matchIdWithName) {
    log.debug("Updating match names={}", matchIdWithName);

    matchIdWithName.forEach((k,v) -> {
      var findResult = repository.findById(k);

      findResult.ifPresent(alert -> {
        alert.setName(v);
        repository.save(alert);
      });
    });
  }
}
