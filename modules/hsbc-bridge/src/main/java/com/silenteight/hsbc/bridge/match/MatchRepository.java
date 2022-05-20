package com.silenteight.hsbc.bridge.match;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface MatchRepository extends Repository<MatchEntity, Long> {

  void save(MatchEntity matchEntity);

  Optional<MatchEntity> findById(long id);

  Collection<MatchEntity> findByNameIn(Collection<String> names);

  @Query(value = "SELECT m.* FROM hsbc_bridge_match m, hsbc_bridge_alert a WHERE a.id = m.alert_id AND a.name IN (:alertNames) ORDER BY a.name ASC", nativeQuery = true)
  List<MatchEntity> findByAlertNames(@Param(value = "alertNames") Collection<String> alertNames);
}
