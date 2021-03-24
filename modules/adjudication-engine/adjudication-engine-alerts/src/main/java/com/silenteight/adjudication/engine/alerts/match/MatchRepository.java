package com.silenteight.adjudication.engine.alerts.match;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import javax.annotation.Nonnull;

interface MatchRepository extends Repository<MatchEntity, Long> {

  @Nonnull
  MatchEntity save(MatchEntity matchEntity);

  Optional<SortIndexOnly> findFirstByAlertIdOrderBySortIndexDesc(long alertId);

  interface SortIndexOnly {

    Integer getSortIndex();
  }
}
