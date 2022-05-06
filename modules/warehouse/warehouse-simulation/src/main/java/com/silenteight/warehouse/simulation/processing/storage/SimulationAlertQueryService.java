package com.silenteight.warehouse.simulation.processing.storage;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

@RequiredArgsConstructor
public class SimulationAlertQueryService {

  @Language("PostgreSQL")
  private static final String GET_SIMULATIONS_FOR_MIGRATION = ""
      + "SELECT DISTINCT "
      + "analysis_name "
      + "FROM warehouse_simulation_alert "
      + "WHERE migrated IS NULL";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<String> getAnalysisNamesForMigration() {
    return jdbcTemplate.query(
        GET_SIMULATIONS_FOR_MIGRATION,
        (rs, rowNum) -> rs.getString("analysis_name"));
  }
}
