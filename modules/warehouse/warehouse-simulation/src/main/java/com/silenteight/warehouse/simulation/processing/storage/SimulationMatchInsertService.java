package com.silenteight.warehouse.simulation.processing.storage;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimulationMatchInsertService {

  private static final String INSERT_SQL = "INSERT into warehouse_simulation_match "
      + "(name, analysis_name, alert_name, payload) "
      + "VALUES (:name, :analysis_name, :alert_name, TO_JSONB(:payload::jsonb))";

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Transactional
  public void insert(Collection<SimulationMatchDefinition> mapWithIndex) {
    SqlParameterSource[] collect = mapWithIndex.stream()
        .map(map -> {
          MapSqlParameterSource params = new MapSqlParameterSource();
          params.addValue("name", map.getMatchName());
          params.addValue("analysis_name", map.getAnalysisName());
          params.addValue("alert_name", map.getAlertName());
          params.addValue("payload", map.getPayload());
          return params;
        })
        .collect(Collectors.toList())
        .toArray(SqlParameterSource[]::new);

    namedParameterJdbcTemplate.batchUpdate(INSERT_SQL, collect);
  }
}
