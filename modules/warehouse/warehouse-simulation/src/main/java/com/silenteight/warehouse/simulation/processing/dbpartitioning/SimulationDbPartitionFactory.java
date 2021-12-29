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

  private static final String SQL_CREATE_PARTITION =
      "CREATE TABLE IF NOT EXISTS %s PARTITION OF warehouse_simulation_alert FOR VALUES IN ('%s')";

  @NonNull
  private final PartitioningSimulationNamingStrategy partitioningSimulationNamingStrategy;

  @NonNull
  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public String createDbPartition(String analysis) {
    String partitionName = partitioningSimulationNamingStrategy.getPartitionName(getId(analysis));
    String sql = format(SQL_CREATE_PARTITION, partitionName, analysis);
    jdbcTemplate.execute(sql);

    return partitionName;
  }
}
