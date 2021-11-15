package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlertWithMatches;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertRegisteredAccessPort;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class AlertRegisteredJdbcDataAccess implements AlertRegisteredAccessPort {

  @Language("PostgreSQL")
  private static final String SQL =
      "select pra.alert_message_id, pra.alert_name, array_agg(prm.match_name)\n"
          + "FROM pb_registered_alert pra\n"
          + "  JOIN pb_registered_match prm ON pra.alert_message_id = prm.alert_message_id\n"
          + "  WHERE pra.alert_message_id IN (:ids)\n"
          + "  GROUP BY pra.alert_message_id, pra.alert_name;";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<RegisteredAlertWithMatches> findRegistered(List<UUID> alertIds) {
    return jdbcTemplate.query(SQL, Map.of("ids", alertIds),
        (rs, rowNum) -> new RegisteredAlertWithMatches(
            UUID.fromString(rs.getString(1)),
            rs.getString(2),
              Arrays.stream((String[]) rs.getArray(3).getArray()).collect(toList())));
  }

}
