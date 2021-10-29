package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.common.model.RegisteredAlert;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class AlertRegisteredJdbcDataAccess {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT pam.system_id, pam.message_id, pra.alert_name, array_agg(prm.match_name)\n"
          + "FROM pb_alert_message pam\n"
          + "         JOIN pb_registered_alert pra USING (alert_message_id)\n"
          + "         JOIN pb_registered_match prm USING (alert_message_id)\n"
          + "WHERE (pam.system_id, pam.message_id) IN (:tuples)\n"
          + "GROUP BY 1, 2, 3";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  List<RegisteredAlert> findRegistered(List<FindRegisteredAlertRequest> registrations) {
    var tuples = registrations.stream()
        .map(r -> new Object[] { r.getSystemId(), r.getMessageId() })
        .collect(toList());

    return jdbcTemplate.query(SQL, Map.of("tuples", tuples),
        (rs, rowNum) -> new RegisteredAlert(
            rs.getString(1), rs.getString(2), rs.getString(3),
            Arrays.stream((String[]) rs.getArray(4).getArray()).collect(toList())));
  }

}
