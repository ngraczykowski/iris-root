package com.silenteight.hsbc.bridge.bulk;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

interface BulkRepository extends Repository<Bulk, UUID> {

  Bulk findById(String id);

  Boolean existsByStatusIn(Collection<BulkStatus> bulkStatuses);

  Bulk save(Bulk bulk);

  @Modifying
  @Query("update Bulk b set b.status = :status where b.id = :id")
  void updateStatusById(@Param("id") String id, @Param("status") BulkStatus status);

  boolean existsById(String id);
}
