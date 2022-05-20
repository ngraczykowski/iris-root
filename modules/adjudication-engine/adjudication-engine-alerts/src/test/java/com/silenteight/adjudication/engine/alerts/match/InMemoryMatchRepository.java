package com.silenteight.adjudication.engine.alerts.match;

import lombok.Value;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryMatchRepository extends BasicInMemoryRepository<MatchEntity>
    implements MatchRepository {

  @Override
  public Stream<LatestSortIndex> findLatestSortIndexByAlertIds(List<Long> alertIds) {
    return stream()
        .filter(m -> alertIds.contains(m.getAlertId()))
        .collect(Collectors.groupingBy(MatchEntity::getAlertId))
        .entrySet()
        .stream()
        .map(entry -> entry
            .getValue().stream()
            .map(MatchEntity::getSortIndex)
            .max(Integer::compareTo)
            .map(sortIndex -> new AlertSortIndexObject(entry.getKey(), sortIndex))
            .orElse(new AlertSortIndexObject(entry.getKey(), 0))
        );
  }

  @Value
  private static class AlertSortIndexObject implements LatestSortIndex {

    Long alertId;
    Integer sortIndex;
  }
}
