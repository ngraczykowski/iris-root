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
class SimulationMatchInsertServiceTest {

  private static final String ANALYSIS_NAME = "storageTest";
  private static final String PAYLOAD = "{}";

  @Autowired
  SimulationDbPartitionFactory simulationDbPartitionFactory;

  @Autowired
  private SimulationMatchInsertService underTest;

  @Autowired
  private JdbcTemplate jdbcTemplate;


  @Test
  void shouldCreatePartitionAndStoreAlertAndMatches() {
    SimulationMatchDefinition matchDefinition = new SimulationMatchDefinition(
        ANALYSIS_NAME,
        Values.ALERT_NAME,
        Values.MATCH_NAME,
        PAYLOAD
    );

    simulationDbPartitionFactory.createDbPartition(ANALYSIS_NAME);
    underTest.insert(List.of(matchDefinition));

    List<SimulationMatchEntity> allEntries = getAllMatches();
    assertThat(allEntries.size()).isEqualTo(1);

    SimulationMatchEntity firstResult = allEntries.get(0);
    assertThat(firstResult.getName()).isEqualTo(Values.MATCH_NAME);
    assertThat(firstResult.getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(firstResult.getAlertName()).isEqualTo(Values.ALERT_NAME);
    assertThat(firstResult.getPayload()).contains(PAYLOAD);
  }

  private List<SimulationMatchEntity> getAllMatches() {
    return jdbcTemplate.query(
        "SELECT name, analysis_name, alert_name, payload FROM warehouse_simulation_match "
            + "WHERE analysis_name = '" + ANALYSIS_NAME + "'", (rs, i) ->
            new SimulationMatchEntity(
                rs.getString("name"),
                rs.getString("alert_name"),
                rs.getString("analysis_name"),
                rs.getString("payload")
            ));
  }

  @Value
  @Builder
  private static class SimulationMatchEntity {

    String name;
    String alertName;
    String analysisName;
    String payload;
  }
}
