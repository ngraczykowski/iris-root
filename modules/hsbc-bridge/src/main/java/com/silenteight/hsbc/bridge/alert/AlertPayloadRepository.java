package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

interface AlertPayloadRepository extends Repository<AlertDataPayloadEntity, Long> {

  @Modifying
  @Query("update AlertDataPayloadEntity a set a.payload = null where a.createdAt < :expireDate")
  void deletePayloadByCreatedAtBefore(@Param("expireDate") OffsetDateTime expireDate);
}
