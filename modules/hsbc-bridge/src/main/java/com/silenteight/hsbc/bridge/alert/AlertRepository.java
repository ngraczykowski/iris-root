package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface AlertRepository extends Repository<AlertEntity, Long> {

  void save(AlertEntity alertEntity);

  Stream<AlertEntity> findByIdIn(Collection<Long> alertIds);

  Optional<AlertEntity> findByName(String name);

  Optional<AlertEntity> findById(Long id);

  Stream<AlertEntity> findByBulkIdAndStatus(String bulkId, AlertStatus status);

  Stream<AlertEntity> findByNameIn(List<String> names);

  @Modifying
  @Query("SELECT a FROM AlertEntity a\n"
      + "WHERE a.name IS NOT NULL "
      + "AND a.status = 'LEARNING_COMPLETED' "
      + "AND (CONCAT(a.externalId, '_', a.discriminator)) IN (:idsWithDiscriminators)\n")
  Stream<AlertEntity> findByNameIsNotNullAndStatusAndExternalIdInAndDiscriminatorIn(
      Collection<String> idsWithDiscriminators);

  @Modifying
  @Query("SELECT a.name FROM AlertEntity a WHERE a.alertTime < :expireDate")
  Stream<String> findAlertEntityNamesByAlertTimeBefore(
      @Param("expireDate") OffsetDateTime expireDate);

  @Modifying
  @Query("UPDATE AlertEntity a SET a.status=:status WHERE a.name IN :names")
  void updateStatusByNames(@Param("status") AlertStatus status, @Param("names") List<String> names);

  @Query("SELECT a FROM AlertEntity a WHERE a.bulkId = :bulkId AND a.externalId IN "
      + "(SELECT b.externalId FROM AlertEntity b WHERE b.bulkId = :bulkId"
      + " GROUP BY b.externalId HAVING COUNT(b.id) > 1) ")
  List<AlertEntity> findDuplicateAlertsByBulkId(@Param("bulkId") String bulkId);
}
