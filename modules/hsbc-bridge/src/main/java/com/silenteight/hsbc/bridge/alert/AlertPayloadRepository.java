package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

interface AlertPayloadRepository extends Repository<AlertDataPayloadEntity, Long> {

  @Modifying
  @Query(value = "UPDATE hsbc_bridge_alert_payload p\n"
      + "SET payload = NULL, updated_at = NOW() \n"
      + "FROM hsbc_bridge_alert a\n"
      + "WHERE p.id = a.alert_payload_id AND p.payload IS NOT NULL AND a.alert_time < :expireDate", nativeQuery = true)
  int deletePayloadByAlertTimeBefore(@Param("expireDate") OffsetDateTime expireDate);
}
