package com.silenteight.warehouse.migration.simulation;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertQueryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = SimulationMigrationTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@Transactional
class SimulationMigrationIT {

  private static final String ANALYSIS_NAME = "analysis/1";
  private static final String ALERT_NAME = "alerts/1";
  private static final String PAYLOAD = "{}";

  @Autowired
  SimulationMigration underTest;

  @Autowired
  SimulationDbPartitionFactory simulationDbPartitionFactory;

  @Autowired
  SimulationAlertInsertService simulationAlertInsertService;

  @Autowired
  SimulationAlertQueryService simulationAlertQueryService;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  void shouldMigrateAlertLevelToMatchLevel() {
    // given
    storeNonMigratedAlert();
    assertThat(getSimulationAlertsWithMatches()).isZero();

    // when
    underTest.migrate();

    // then
    assertThat(getSimulationAlertsWithMatches()).isNotZero();
  }

  private void storeNonMigratedAlert() {
    simulationDbPartitionFactory.createDbPartition(ANALYSIS_NAME);
    SimulationAlertDefinition simulationAlertDefinition =
        new SimulationAlertDefinition(ANALYSIS_NAME, ALERT_NAME, PAYLOAD, emptyList(), null);
    simulationAlertInsertService.insert(of(simulationAlertDefinition));
  }

  private Integer getSimulationAlertsWithMatches() {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) as sim_alert_with_match_count "
            + "FROM warehouse_simulation_alert wsa "
            + "JOIN warehouse_simulation_match wsm ON wsa.name=wsm.alert_name",
        (rs, rowNum) -> rs.getInt("sim_alert_with_match_count"));
  }
}
