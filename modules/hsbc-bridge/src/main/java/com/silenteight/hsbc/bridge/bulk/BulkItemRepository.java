package com.silenteight.hsbc.bridge.bulk;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

interface BulkItemRepository extends Repository<BulkItem, Long> {

  Optional<BulkItem> findById(long bulkItemId);

  void save(BulkItem bulkItem);

  @Modifying
  @Query("update BulkItem b set b.payload = null where b.createdAt < :expireDate")
  void deletePayloadByCreatedAtBefore(@Param("expireDate") OffsetDateTime expireDate);
}
