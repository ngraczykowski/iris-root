package com.silenteight.warehouse.retention.simulation;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.test.client.gateway.AnalysisExpiredClientGateway;
import com.silenteight.warehouse.test.client.gateway.SimulationV1IndexClientGateway;
import com.silenteight.warehouse.test.client.listener.sim.IndexedSimEventListener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.retention.simulation.RetentionSimulationFixtures.ANALYSIS_EXPIRED_REQUEST;
import static com.silenteight.warehouse.retention.simulation.RetentionSimulationFixtures.ANALYSIS_NAME;
import static com.silenteight.warehouse.retention.simulation.RetentionSimulationFixtures.SIMULATION_DATA_INDEX_REQUEST;
import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = RetentionSimulationTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class
})
@ActiveProfiles("jpa-test")
class AnalysisExpiredIT {

  private static final int TIMEOUT = 5;
  private static final String SELECT_NAME_QUERY = "SELECT name FROM warehouse_simulation_alert";

  @Autowired
  private AnalysisExpiredClientGateway analysisExpiredClientGateway;
  @Autowired
  private SimulationV1IndexClientGateway simulationV1IndexClientGateway;
  @Autowired
  private IndexedSimEventListener indexedSimEventListener;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @AfterEach
  void cleanUp() {
    indexedSimEventListener.clear();
  }

  @SneakyThrows
  @Test
  void shouldRemoveAnalysisWhenAnalysisExpiredReceived() {
    // given
    simulationV1IndexClientGateway.indexRequest(SIMULATION_DATA_INDEX_REQUEST);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> this.indexedSimEventListener.hasAnyEvent());

    assertThat(simAlertTableHasRecords()).isTrue();

    // when
    analysisExpiredClientGateway.indexRequest(ANALYSIS_EXPIRED_REQUEST);
    await()
        .atMost(5, SECONDS)
        .until(() -> !simAlertTableHasRecords());

    // then
    assertThat(simAlertTableHasRecords()).isFalse();
  }

  @SneakyThrows
  @Test
  void shouldHandleNonExistingAnalysisGracefully() {
    // given
    simulationV1IndexClientGateway.indexRequest(SIMULATION_DATA_INDEX_REQUEST);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> this.indexedSimEventListener.hasAnyEvent());

    assertThat(simAlertTableHasRecords()).isTrue();

    // when
    AnalysisExpired analysisExpired = AnalysisExpired.newBuilder()
        .addAllAnalysis(of("analysis/non-existing", ANALYSIS_NAME, "analysis/non-existing"))
        .build();
    analysisExpiredClientGateway.indexRequest(analysisExpired);
    await()
        .atMost(5, SECONDS)
        .until(() -> !simAlertTableHasRecords());

    // then
    assertThat(simAlertTableHasRecords()).isFalse();
  }

  private boolean simAlertTableHasRecords() {
    List<String> names = jdbcTemplate.queryForList(SELECT_NAME_QUERY, String.class);
    return names.size() > 0;
  }
}
