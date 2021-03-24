package com.silenteight.hsbc.bridge.match;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

interface MatchRepository extends Repository<MatchEntity, Long> {

  void save(MatchEntity matchEntity);

  MatchEntity findById(long id);

  @Modifying
  @Query("update MatchEntity m set m.name=:name where m.id=:id")
  void updateNameById(@Param("id")long id, @Param("name")String name);

}
