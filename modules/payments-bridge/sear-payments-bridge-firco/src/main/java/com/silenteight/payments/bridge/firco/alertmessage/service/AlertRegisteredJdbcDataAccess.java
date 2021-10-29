package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertRegistration;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class AlertRegisteredJdbcDataAccess {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT a.system_id, a.message_id FROM pb_alert_message a "
          + "WHERE (a.system_id, a.message_id) IN (:tuples)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  List<AlertRegistration> findRegistered(List<AlertRegistration> registrations) {
    var tuples = registrations.stream()
        .map(r -> new Object[] {r.getSystemId(), r.getMessageId()})
        .collect(toList());

    return jdbcTemplate.query(SQL, Map.of("tuples", tuples),
        (rs, rowNum) -> new AlertRegistration(
            rs.getString(1), rs.getString(2)));
  }

}
