package com.silenteight.payments.bridge.firco.alertmessage.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertIdSet;
import com.silenteight.payments.bridge.firco.alertmessage.port.FindAlertIdSetAccessPort;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class FindAlertIdSetJdbcDataAccess implements FindAlertIdSetAccessPort {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT pam.alert_message_id, pam.system_id, pam.message_id FROM pb_alert_message pam\n"
          + "WHERE pam.system_id IN (:systemIds)\n";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<AlertIdSet> find(List<String> systemIds) {
    if (systemIds.isEmpty()) {
      return List.of();
    }

    return jdbcTemplate.query(SQL, Map.of("messageIds", systemIds),
        (rs, rowNum) -> new AlertIdSet(
            UUID.fromString(rs.getString(1)),
            rs.getString(2),
            rs.getString(3)));
  }
}
