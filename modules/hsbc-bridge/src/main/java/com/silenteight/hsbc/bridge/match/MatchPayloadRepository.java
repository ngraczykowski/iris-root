package com.silenteight.hsbc.bridge.match;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

interface MatchPayloadRepository extends Repository<MatchPayloadEntity, Long> {

  @Modifying
  @Query(value = "UPDATE hsbc_bridge_match_payload p\n"
      + "SET payload = NULL\n"
      + "FROM hsbc_bridge_alert a, hsbc_bridge_match m\n"
      + "WHERE p.id = m.match_payload_id AND m.alert_id = a.id AND p.payload IS NOT NULL AND a.alert_time < :expireDate", nativeQuery = true)
  int deletePayloadByAlertTimeBefore(@Param("expireDate") OffsetDateTime expireDate);
}
