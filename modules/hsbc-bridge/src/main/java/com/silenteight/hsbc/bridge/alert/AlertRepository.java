package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);

  List<AlertEntity> findByIdIn(Collection<Long> alertIds);

  Optional<AlertEntity> findByName(String name);

  Optional<AlertEntity> findById(Long id);

  Stream<AlertEntity> findByBulkIdAndStatus(String bulkId, AlertStatus status);

  Stream<AlertEntity> findByNameIn(List<String> names);

  @Modifying
  @Query("update AlertEntity a set a.status=:status where a.name IN :names")
  void updateStatusByNames(@Param("status") AlertStatus status, @Param("names") List<String> names);
}
