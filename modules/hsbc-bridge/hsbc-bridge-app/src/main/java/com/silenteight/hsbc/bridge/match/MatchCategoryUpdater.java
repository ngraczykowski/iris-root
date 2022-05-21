package com.silenteight.hsbc.bridge.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Slf4j
class MatchCategoryUpdater {

  private final EntityManager entityManager;
  private final MatchRepository repository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateNames(@NonNull Map<Long, String> matchIdWithName) {
    log.debug("Updating match names={}", matchIdWithName);

    matchIdWithName.forEach((k, v) -> {
      var findResult = repository.findById(k);

      findResult.ifPresent(alert -> {
        alert.setName(v);
        alert.setMatchName(getMatchName(v));
        repository.save(alert);
      });

      entityManager.createQuery(
          "UPDATE MatchCategoryEntity m SET m.name = CONCAT(m.name, '/', :matchName) WHERE m.matchId = :matchId")
          .setParameter("matchName", v)
          .setParameter("matchId", k)
          .executeUpdate();
    });
  }

  private String getMatchName(String alertIdWIthMatchId) {
    return "matches/" + alertIdWIthMatchId.substring(alertIdWIthMatchId.lastIndexOf('/') + 1);
  }
}
