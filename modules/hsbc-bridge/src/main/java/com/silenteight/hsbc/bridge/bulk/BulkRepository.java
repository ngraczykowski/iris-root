package com.silenteight.hsbc.bridge.bulk;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface BulkRepository extends Repository<Bulk, UUID> {

  Optional<Bulk> findById(String id);

  Optional<Bulk> findByAnalysisId(long analysisId);

  Boolean existsByStatusIn(Collection<BulkStatus> bulkStatuses);

  Bulk save(Bulk bulk);

  @Modifying
  @Query("update Bulk b set b.status = :status where b.id = :id")
  void updateStatusById(@Param("id") String id, @Param("status") BulkStatus status);

  boolean existsById(String id);

  Collection<Bulk> findByStatus(BulkStatus status);
}
