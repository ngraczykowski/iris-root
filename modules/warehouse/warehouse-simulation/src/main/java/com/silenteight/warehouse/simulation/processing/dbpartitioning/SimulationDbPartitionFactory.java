package com.silenteight.warehouse.simulation.processing.dbpartitioning;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.warehouse.simulation.processing.dbpartitioning.NameResource.getId;
import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class SimulationDbPartitionFactory {

  private static final String SQL_CREATE_ALERT_PARTITION =
      "CREATE TABLE IF NOT EXISTS %s PARTITION OF warehouse_simulation_alert FOR VALUES IN ('%s')";

  private static final String SQL_CREATE_MATCH_PARTITION =
      "CREATE TABLE IF NOT EXISTS %s PARTITION OF warehouse_simulation_match FOR VALUES IN ('%s')";

  @NonNull
  private final PartitioningSimulationNamingStrategy partitioningSimulationNamingStrategy;

  @NonNull
  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void createDbPartition(String analysis) {
    String alertPartitionName =
        partitioningSimulationNamingStrategy.getPartitionName(getId(analysis));
    String alertSql = format(SQL_CREATE_ALERT_PARTITION, alertPartitionName, analysis);
    jdbcTemplate.execute(alertSql);

    String matchPartitionName =
        partitioningSimulationNamingStrategy.getMatchPartitionName(getId(analysis));
    String matchSql = format(SQL_CREATE_MATCH_PARTITION, matchPartitionName, analysis);
    jdbcTemplate.execute(matchSql);
  }
}
