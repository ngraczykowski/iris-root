package com.silenteight.payments.bridge.firco.alertmessage.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertFircoId;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertIdSet;
import com.silenteight.payments.bridge.firco.alertmessage.port.FindAlertIdSetAccessPort;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class FindAlertIdSetJdbcDataAccess implements FindAlertIdSetAccessPort {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT pam.alert_message_id, pam.system_id, pam.message_id FROM pb_alert_message pam\n"
          + "WHERE (pam.system_id, pam.message_id) IN (:tuples)\n";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<AlertIdSet> find(List<AlertFircoId> alertIds) {
    if (alertIds.isEmpty()) {
      return List.of();
    }

    var tuples = alertIds.stream()
        .map(r -> new Object[] { r.getSystemId(), r.getMessageId() })
        .collect(toList());

    return jdbcTemplate.query(SQL, Map.of("tuples", tuples),
        (rs, rowNum) -> new AlertIdSet(
            UUID.fromString(rs.getString(1)),
            rs.getString(2),
            rs.getString(3)));
  }


}
