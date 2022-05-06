package com.silenteight.warehouse.simulation.processing.storage;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimulationAlertInsertService {

  private static final String INSERT_SQL = "INSERT into warehouse_simulation_alert "
      + "(name, analysis_name, payload, migrated) "
      + "VALUES (:name, :analysis_name, TO_JSONB(:payload::jsonb), :migrated)";

  private static final String INSERT_NONMIGRATED_SQL = "INSERT into warehouse_simulation_alert "
      + "(name, analysis_name, payload) "
      + "VALUES (:name, :analysis_name, TO_JSONB(:payload::jsonb))";

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public void insert(Collection<SimulationAlertDefinition> mapWithIndex) {
    SqlParameterSource[] collect = mapWithIndex.stream()
        .map(map -> {
          MapSqlParameterSource params = new MapSqlParameterSource();
          params.addValue("name", map.getAlertName());
          params.addValue("analysis_name", map.getAnalysisName());
          params.addValue("payload", map.getPayload());
          params.addValue("migrated", map.getMigrated());
          return params;
        })
        .collect(Collectors.toList())
        .toArray(SqlParameterSource[]::new);

    namedParameterJdbcTemplate.batchUpdate(INSERT_SQL, collect);
  }

  /**
   * @deprecated To be removed V1 payload should be stored at both alert and match level and marked
   *     as migrated (WH-367)
   */
  @Deprecated(forRemoval = true)
  public void insertNonMigrated(Collection<SimulationAlertDefinition> mapWithIndex) {
    SqlParameterSource[] collect = mapWithIndex.stream()
        .map(map -> {
          MapSqlParameterSource params = new MapSqlParameterSource();
          params.addValue("name", map.getAlertName());
          params.addValue("analysis_name", map.getAnalysisName());
          params.addValue("payload", map.getPayload());
          return params;
        })
        .collect(Collectors.toList())
        .toArray(SqlParameterSource[]::new);

    namedParameterJdbcTemplate.batchUpdate(INSERT_NONMIGRATED_SQL, collect);
  }
}
