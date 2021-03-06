package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertRegisteredAccessPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
class AlertRegisteredJdbcDataAccess implements AlertRegisteredAccessPort {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT "
          + "pra.alert_message_id,pra.fkco_system_id,\n"
          + "       pra.alert_name,\n"
          + "       to_jsonb(json_agg(json_build_object('matchId', prm.match_id, 'matchName',\n"
          + "                                           prm.match_name))) AS matches\n"
          + "FROM pb_registered_alert pra\n"
          + "         JOIN pb_registered_match prm "
          + "ON pra.registered_alert_id = prm.registered_alert_id\n"
          + "WHERE pra.fkco_system_id IN (:systemIds)\n"
          + "GROUP BY pra.alert_message_id,pra.fkco_system_id, pra.alert_name;";

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;
  private final CollectionType matchListType;

  AlertRegisteredJdbcDataAccess(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.objectMapper = new ObjectMapper();
    this.matchListType = this.objectMapper
        .getTypeFactory()
        .constructCollectionType(ArrayList.class, RegisteredMatch.class);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public List<RegisteredAlert> findRegistered(List<String> systemIds) {

    return jdbcTemplate.query(SQL, Map.of("systemIds", systemIds),
        (rs, rowNum) -> new RegisteredAlert(
            rs.getString("alert_message_id"),
            rs.getString("fkco_system_id"),
            rs.getString("alert_name"),
            deserializeRegisteredMatch(rs)));
  }


  private List<RegisteredMatch> deserializeRegisteredMatch(ResultSet rs) throws SQLException {
    try {
      return objectMapper.readValue(rs.getString("matches"), matchListType);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
