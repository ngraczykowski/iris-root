package com.silenteight.warehouse.indexer;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.analysis.TestAnalysisMetadataRepository;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.IndexedEventListener;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.SIMULATION_ANALYSIS_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SIMULATION_ANALYSIS_ID;
import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
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

  static final String PRODUCTION_INDEX_NAME = "itest_production";
  static final String SIMULATION_INDEX_NAME = "itest_simulation_" + SIMULATION_ANALYSIS_ID;

  @BeforeEach
  void init() {
    indexedEventListener.clear();
  }

  @AfterEach
  void cleanup() {
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
    removeData();
  }

  @Autowired
  private TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private SimulationIndexClientGateway simulationIndexClientGateway;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Test
  void shouldReturnConfirmationWhenProductionDataIndexRequested() {
    ProductionDataIndexRequest request = ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(of(ALERT_1))
        .setRequestId(UUID.randomUUID().toString())
        .build();

    productionIndexClientGateway.indexRequest(request);

    await()
        .atMost(5, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent())
        .get()
        .extracting(DataIndexResponse::getRequestId)
        .isEqualTo(request.getRequestId());

    var source = simpleElasticTestClient.getSource(PRODUCTION_INDEX_NAME, DOCUMENT_ID);
    assertThat(source).isEqualTo(MAPPED_ALERT_1);
  }

  @Test
  void shouldReturnConfirmationWhenSimulationDataIndexRequested() {
    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .setAnalysisName(SIMULATION_ANALYSIS_NAME)
        .addAllAlerts(of(ALERT_1))
        .build();

    simulationIndexClientGateway.indexRequest(request);

    await()
        .atMost(5, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent())
        .get()
        .extracting(DataIndexResponse::getRequestId)
        .isEqualTo(request.getRequestId());

    var source = simpleElasticTestClient.getSource(SIMULATION_INDEX_NAME, DOCUMENT_ID);
    assertThat(source).isEqualTo(MAPPED_ALERT_1);
  }

  private void removeData() {
    safeDeleteIndex(SIMULATION_INDEX_NAME);
    safeDeleteIndex(PRODUCTION_INDEX_NAME);
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }
}
