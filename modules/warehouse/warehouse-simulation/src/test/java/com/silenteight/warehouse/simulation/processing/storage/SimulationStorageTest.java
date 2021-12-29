package com.silenteight.warehouse.simulation.processing.storage;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = StorageTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@Transactional
class SimulationStorageTest {

  private static final String ANALYSIS_NAME = "storageTest";
  private static final String PAYLOAD = "{}";

  @Autowired
  SimulationDbPartitionFactory simulationDbPartitionFactory;

  @Autowired
  private SimulationAlertInsertService underTest;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldCreatePartitionAndStoreAlert() {
    SimulationAlertDefinition definition = new SimulationAlertDefinition(
        ANALYSIS_NAME,
        Values.ALERT_NAME,
        PAYLOAD
    );

    simulationDbPartitionFactory.createDbPartition(ANALYSIS_NAME);
    underTest.insert(List.of(definition));

    List<SimulationAlertEntity> allEntries = getAllEntries();
    System.out.println(getAllEntries());
    assertThat(allEntries.size()).isEqualTo(1);

    SimulationAlertEntity firstResult = allEntries.get(0);
    assertThat(firstResult.getName()).isEqualTo(Values.ALERT_NAME);
    assertThat(firstResult.getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(firstResult.getPayload()).contains(PAYLOAD);
  }

  private List<SimulationAlertEntity> getAllEntries() {
    return jdbcTemplate.query(
        "SELECT name, analysis_name, payload FROM warehouse_simulation_alert "
            + "WHERE analysis_name = '" + ANALYSIS_NAME + "'", (rs, i) ->
            new SimulationAlertEntity(
                rs.getString("name"),
                rs.getString("analysis_name"),
                rs.getString("payload")
            ));
  }

  @Value
  @Builder
  private static class SimulationAlertEntity {

    String name;
    String analysisName;
    String payload;
  }
}
