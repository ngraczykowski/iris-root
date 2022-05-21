package com.silenteight.hsbc.bridge.alert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Set;

interface AlertPayloadRepository extends Repository<AlertDataPayloadEntity, Long> {

  @Modifying
  @Query(value = "UPDATE hsbc_bridge_alert_payload p "
      + "SET payload = NULL, updated_at = NOW() "
      + "FROM hsbc_bridge_alert a "
      + "WHERE p.id = a.alert_payload_id AND p.payload IS NOT NULL AND a.alert_time < :expireDate", nativeQuery = true)
  int deletePayloadByAlertTimeBefore(@Param("expireDate") OffsetDateTime expireDate);

  @Query(value = "SELECT a.name "
      + "FROM hsbc_bridge_alert_payload p "
      + "JOIN hsbc_bridge_alert a ON p.id = a.alert_payload_id "
      + "WHERE p.payload IS NOT NULL AND a.alert_time < :expireDate", nativeQuery = true)
  Set<String> getAlertNamesByAlertTimeBefore(@Param("expireDate") OffsetDateTime expireDate);
}
