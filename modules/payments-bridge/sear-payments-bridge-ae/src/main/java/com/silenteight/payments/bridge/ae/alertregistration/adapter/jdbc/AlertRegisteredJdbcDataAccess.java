package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlertWithMatches;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertRegisteredAccessPort;

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
import java.util.UUID;

@Component
class AlertRegisteredJdbcDataAccess implements AlertRegisteredAccessPort {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT pra.alert_message_id,\n"
          + "       pra.alert_name,\n"
          + "       to_jsonb(json_agg(json_build_object('matchId', prm.match_id, 'matchName',\n"
          + "                                           prm.match_name))) AS matches\n"
          + "FROM pb_registered_alert pra\n"
          + "         JOIN pb_registered_match prm ON pra.alert_message_id = prm.alert_message_id\n"
          + "WHERE pra.alert_message_id IN (:ids)\n"
          + "GROUP BY pra.alert_message_id, pra.alert_name;";

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

  public List<RegisteredAlertWithMatches> findRegistered(List<UUID> alertIds) {
    return jdbcTemplate.query(SQL, Map.of("ids", alertIds),
        (rs, rowNum) -> new RegisteredAlertWithMatches(
            UUID.fromString(rs.getString(1)),
            rs.getString(2),
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
