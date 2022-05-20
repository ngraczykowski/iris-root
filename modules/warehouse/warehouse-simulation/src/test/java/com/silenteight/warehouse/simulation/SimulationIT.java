package com.silenteight.warehouse.simulation;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.e2e.CleanDatabase;
import com.silenteight.warehouse.test.client.gateway.SimulationV1IndexClientGateway;
import com.silenteight.warehouse.test.client.gateway.SimulationV2IndexClientGateway;
import com.silenteight.warehouse.test.client.listener.sim.IndexedSimEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.SIMULATION_ANALYSIS_NAME;
import static com.silenteight.warehouse.simulation.SimulationAlertFixtures.ALERT_SIM_1;
import static com.silenteight.warehouse.simulation.SimulationAlertFixtures.ALERT_SIM_2;
import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = com.silenteight.warehouse.simulation.SimulationTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@CleanDatabase
class SimulationIT {

  private static final int TIMEOUT = 5;

  @Autowired
  private IndexedSimEventListener indexedSimEventListener;

  @Autowired
  private SimulationV1IndexClientGateway simulationV1IndexClientGateway;

  @Autowired
  private SimulationV2IndexClientGateway simulationV2IndexClientGateway;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void init() {
    indexedSimEventListener.clear();
  }

  @Test
  void shouldReturnConfirmationWhenSimulationDataIndexV1Requested() {
    String requestId = UUID.randomUUID().toString();
    var request = com.silenteight.data.api.v1.SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(SIMULATION_ANALYSIS_NAME)
        .addAllAlerts(of(ALERT_SIM_1))
        .build();

    simulationV1IndexClientGateway.indexRequest(request);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> this.indexedSimEventListener.hasAnyEvent());
    assertThat(this.indexedSimEventListener.getLastEventId()).isPresent();
    assertThat(this.indexedSimEventListener.getLastEventId().get())
        .isEqualTo(request.getRequestId());
    assertThat(countSimulationAlertsWithMatches()).isEqualTo(1);
  }


  @Test
  void shouldReturnConfirmationWhenSimulationDataIndexV2Requested() {
    String requestId = UUID.randomUUID().toString();
    var request = com.silenteight.data.api.v2.SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(SIMULATION_ANALYSIS_NAME)
        .addAllAlerts(of(ALERT_SIM_2))
        .build();

    simulationV2IndexClientGateway.indexRequest(request);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> this.indexedSimEventListener.hasAnyEvent());
    assertThat(this.indexedSimEventListener.getLastEventId()).isPresent();
    assertThat(this.indexedSimEventListener.getLastEventId().get())
        .isEqualTo(request.getRequestId());

    assertThat(countSimulationAlertsWithMatches()).isEqualTo(1);
  }

  private Integer countSimulationAlertsWithMatches() {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) as sim_alert_with_match_count "
            + "FROM warehouse_simulation_alert wsa "
            + "JOIN warehouse_simulation_match wsm ON wsa.name=wsm.alert_name",
        (rs, rowNum) -> rs.getInt("sim_alert_with_match_count"));
  }
}
