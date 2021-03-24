package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);

  List<AlertEntity> findByIdIn(Collection<Long> alertIds);

  @Modifying
  @Query("update AlertEntity a set a.name=:name where a.id=:id")
  void updateNameById(@Param("id") long id, @Param("name") String name);
}
