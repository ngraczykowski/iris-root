package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);

  List<AlertEntity> findByIdIn(Collection<Long> alertIds);

  Optional<AlertEntity> findByName(String name);

  Optional<AlertEntity> findById(Long id);

  Optional<AlertEntity> findByBulkItemId(long bulkItemId);

  @Modifying
  @Query("update AlertEntity a set a.payload = null where a.createdAt < :expireDate")
  void deletePayloadByCreatedAtBefore(@Param("expireDate") OffsetDateTime expireDate);
}
