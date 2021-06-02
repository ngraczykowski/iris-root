package com.silenteight.warehouse.indexer;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.analysis.TestAnalysisMetadataRepository;
import com.silenteight.warehouse.indexer.indextestclient.gateway.IndexClientGateway;
import com.silenteight.warehouse.indexer.indextestclient.listener.IndexedEventListener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERTS_WITH_MATCHES;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ANALYSIS_RESOURCE_PREFIX;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = IndexerTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    OpendistroElasticContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
class IndexerIT {

  private static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";

  @BeforeEach
  void init() {
    indexedEventListener.clear();
  }

  @AfterEach
  void cleanup() {
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
  }

  @Autowired
  private TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @Autowired
  private IndexClientGateway productionIndexClientGateway;

  @Autowired
  private IndexClientGateway simulationIndexClientGateway;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Test
  void shouldReturnConfirmationWhenProductionDataIndexRequested() {
    ProductionDataIndexRequest request = ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(ALERTS_WITH_MATCHES)
        .build();

    productionIndexClientGateway.indexRequest(request);

    await()
        .atMost(5, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent())
        .get()
        .extracting(DataIndexResponse::getRequestId)
        .isEqualTo(request.getRequestId());

    String expectedElasticIndexName = "itest_production";
    var source = simpleElasticTestClient.getSource(expectedElasticIndexName, DOCUMENT_ID);
    assertThat(source).isEqualTo(ALERT_WITH_MATCHES_1_MAP);
  }

  @Test
  void shouldReturnConfirmationWhenSimulationDataIndexRequested() {
    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .setAnalysisName(ANALYSIS_RESOURCE_PREFIX + SIMULATION_ANALYSIS_ID)
        .addAllAlerts(ALERTS_WITH_MATCHES)
        .build();

    simulationIndexClientGateway.indexRequest(request);

    await()
        .atMost(5, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent())
        .get()
        .extracting(DataIndexResponse::getRequestId)
        .isEqualTo(request.getRequestId());

    String expectedElasticIndexName = "itest_simulation_" + SIMULATION_ANALYSIS_ID;
    var source = simpleElasticTestClient.getSource(expectedElasticIndexName, DOCUMENT_ID);
    assertThat(source).isEqualTo(ALERT_WITH_MATCHES_1_MAP);
  }
}
