package com.silenteight.hsbc.bridge.match;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface MatchPayloadRepository extends Repository<MatchPayloadEntity, Long> {

  @Modifying
  @Query("update MatchPayloadEntity m set m.payload = null where m.createdAt < :expireDate")
  void deletePayloadByCreatedAtBefore(@Param("expireDate") OffsetDateTime expireDate);
}
