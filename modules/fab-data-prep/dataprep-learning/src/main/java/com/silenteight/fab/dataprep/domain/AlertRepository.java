package com.silenteight.fab.dataprep.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
interface AlertRepository extends CrudRepository<AlertEntity, Long>, AlertRepositoryExt {

  @Modifying(clearAutomatically = true)
  @Query("update AlertEntity set state = :state"
      + " where discriminator = :discriminator and createdAt > :createdAfter")
  void updateState(
      @Param("discriminator") String discriminator,
      @Param("state") AlertEntity.State state,
      @Param("createdAfter") OffsetDateTime createdAfter);

  Optional<AlertEntity> findByDiscriminatorAndCreatedAtAfter(
      String discriminator,
      OffsetDateTime createdAt);
}
