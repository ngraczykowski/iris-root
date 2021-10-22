package com.silenteight.adjudication.engine.alerts.match;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

interface MatchRepository extends Repository<MatchEntity, Long> {

  @Nonnull
  Iterable<MatchEntity> saveAll(Iterable<MatchEntity> matchEntities);

  @Query(value =
      "SELECT m.alert_id AS alertId, MAX(m.sort_index) AS sortIndex"
          + " FROM ae_match m"
          + " WHERE m.alert_id IN :alertIds"
          + " GROUP BY m.alert_id", nativeQuery = true)
  Stream<LatestSortIndex> findLatestSortIndexByAlertIds(List<Long> alertIds);

  interface LatestSortIndex {

    Long getAlertId();

    Integer getSortIndex();
  }
}
