package com.silenteight.warehouse.simulation.processing.storage;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimulationAlertInsertService {

  @Language("PostgreSQL")
  private static final String INSERT_SQL = "INSERT INTO warehouse_simulation_alert "
      + "(name, analysis_name, payload, migrated) "
      + "VALUES (:name, :analysis_name, TO_JSONB(:payload::jsonb), :migrated)";

  @Language("PostgreSQL")
  private static final String INSERT_NONMIGRATED_SQL = "INSERT INTO warehouse_simulation_alert "
      + "(name, analysis_name, payload) "
      + "VALUES (:name, :analysis_name, TO_JSONB(:payload::jsonb))";

  @Language("PostgreSQL")
  private static final String SET_MIGRATION_FLAG = "UPDATE warehouse_simulation_alert "
      + " SET migrated=:migrated "
      + " WHERE analysis_name=:analysis_name";

  private static final String MIGRATE_DATA = "INSERT INTO warehouse_simulation_match "
      + "(name, analysis_name, alert_name, payload) "
      + "SELECT "
      + " name as match_name,"
      + " analysis_name as analysis_name,"
      + " name as alert_name,"
      + " payload as payload "
      + "FROM warehouse_simulation_alert "
      + "WHERE analysis_name=:analysis_name";

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

  public int migrateAlertToMatch(String analysisName) {
    return namedParameterJdbcTemplate.update(
        MIGRATE_DATA,
        Map.of("analysis_name", analysisName));
  }

  public int setMigrationFlag(String analysisName, boolean migrated) {
    return namedParameterJdbcTemplate.update(SET_MIGRATION_FLAG, Map.of(
        "analysis_name", analysisName,
        "migrated", migrated
    ));
  }
}
