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
      + " where messageName = :messageName and createdAt > :createdAfter")
  void updateState(
      @Param("messageName") String messageName,
      @Param("state") AlertEntity.State state,
      @Param("createdAfter") OffsetDateTime createdAfter);

  Optional<AlertEntity> findByMessageNameAndCreatedAtAfter(
      String messageName,
      OffsetDateTime createdAt);
}
