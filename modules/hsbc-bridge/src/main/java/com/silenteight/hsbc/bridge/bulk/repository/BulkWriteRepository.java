package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.Bulk;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BulkWriteRepository {

  Bulk save(Bulk bulk);

  @Modifying
  @Query("update Bulk b set b.status = :status where b.id = :id")
  void updateStatusById(@Param("id") UUID id, @Param("status") BulkStatus status);
}
