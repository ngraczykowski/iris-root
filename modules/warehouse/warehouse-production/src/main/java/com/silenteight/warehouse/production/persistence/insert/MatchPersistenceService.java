package com.silenteight.warehouse.production.persistence.insert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.production.persistence.mapping.match.MatchDefinition;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class MatchPersistenceService {

  private static final String ALERT_ID_PARAMETER = "alertId";
  private static final String MATCH_ID_PARAMETER = "matchId";
  private static final String NAME_PARAMETER = "name";
  private static final String PAYLOAD_PARAMETER = "payload";

  private static final String INSERT_MATCH_SQL =
      " INSERT INTO warehouse_match (alert_id, match_id, name, payload)"
          + " VALUES (:alertId,:matchId,:name,TO_JSON(:payload::json))"
          + " ON CONFLICT (alert_id, match_id) DO UPDATE SET"
          + " payload = warehouse_match.payload || excluded.payload";

  void persist(
      NamedParameterJdbcTemplate jdbcTemplate,
      long persistedAlertId,
      List<MatchDefinition> matchDefinitions) {

    MapSqlParameterSource[] parameters = mapToSqlParameters(persistedAlertId, matchDefinitions);
    jdbcTemplate.batchUpdate(INSERT_MATCH_SQL, parameters);
    log.debug("Persisted match for persistedAlertId:{}, matchDefinitions:{}",
        persistedAlertId, matchDefinitions);
  }

  private MapSqlParameterSource[] mapToSqlParameters(
      long persistedAlertId, List<MatchDefinition> matchDefinitions) {

    return matchDefinitions
        .stream()
        .map(match -> toSqlParameters(persistedAlertId, match))
        .toArray(MapSqlParameterSource[]::new);
  }

  private static MapSqlParameterSource toSqlParameters(
      long persistedAlertId, MatchDefinition matchDefinition) {

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue(ALERT_ID_PARAMETER, persistedAlertId);
    parameters.addValue(MATCH_ID_PARAMETER, matchDefinition.getMatchId());
    parameters.addValue(NAME_PARAMETER, matchDefinition.getName());
    parameters.addValue(PAYLOAD_PARAMETER, matchDefinition.getPayload());
    return parameters;
  }
}
