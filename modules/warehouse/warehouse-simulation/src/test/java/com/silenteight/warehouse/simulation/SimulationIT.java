package com.silenteight.warehouse.simulation;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.test.client.gateway.SimulationV1IndexClientGateway;
import com.silenteight.warehouse.test.client.listener.sim.IndexedSimEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.SIMULATION_ANALYSIS_NAME;
import static com.silenteight.warehouse.simulation.SimulationAlertFixtures.ALERT_SIM_1;
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
@Transactional
@DirtiesContext
class SimulationIT {

  private static final int TIMEOUT = 5;

  @Autowired
  private IndexedSimEventListener indexedSimEventListener;

  @Autowired
  private SimulationV1IndexClientGateway simulationV1IndexClientGateway;

  @BeforeEach
  void init() {
    indexedSimEventListener.clear();
  }

  @Test
  void shouldReturnConfirmationWhenSimulationDataIndexV1Requested() {
    String requestId = UUID.randomUUID().toString();
    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(SIMULATION_ANALYSIS_NAME)
        .addAllAlerts(of(ALERT_SIM_1))
        .build();

    simulationV1IndexClientGateway.indexRequest(request);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> this.indexedSimEventListener.hasAnyEvent());
    assertThat(this.indexedSimEventListener.getLastEvent()).isPresent();
    assertThat(this.indexedSimEventListener.getLastEvent().get().getRequestId())
        .isEqualTo(request.getRequestId());
  }
}
