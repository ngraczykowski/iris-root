package com.silenteight.bridge.core.registration.adapter.outgoing;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudMatchRepository extends CrudRepository<MatchEntity, Long> {

  @Modifying
  @Query("UPDATE matches "
      + "SET status = :status, updated_at = NOW() "
      + "WHERE name IN(:names)")
  void updateStatusByNameIn(MatchEntity.Status status, List<String> names);
}
