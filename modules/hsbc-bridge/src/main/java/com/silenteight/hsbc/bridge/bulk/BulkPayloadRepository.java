package com.silenteight.hsbc.bridge.bulk;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;

interface BulkPayloadRepository extends Repository<BulkPayloadEntity, Long> {

  @Modifying
  @Query("update BulkPayloadEntity b set b.payload = null where b.createdAt < :expireDate")
  void deletePayloadByCreatedAtBefore(OffsetDateTime expireDate);
}
