package com.silenteight.adjudication.engine.alerts.match;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Optional;

import static java.util.Comparator.comparingInt;

public class InMemoryMatchRepository extends BasicInMemoryRepository<MatchEntity>
    implements MatchRepository {

  @Override
  public Optional<SortIndexOnly> findFirstByAlertIdOrderBySortIndexDesc(long alertId) {
    return stream()
        .filter(m -> m.getAlertId() == alertId)
        .max(comparingInt(MatchEntity::getSortIndex))
        .map(m -> m::getSortIndex);
  }
}
