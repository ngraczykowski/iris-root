package com.silenteight.warehouse.indexer;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.indexer.simulation.analysis.TestAnalysisMetadataRepository;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.prod.IndexedEventListener;
import com.silenteight.warehouse.test.client.listener.sim.IndexedSimEventListener;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.silenteight.warehouse.indexer.IndexerFixtures.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_11;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.SIMULATION_ANALYSIS_NAME;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
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
@Disabled
class IndexerIT {

  private static final int TIMEOUT = 5;
  @Autowired
  private TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private SimulationIndexClientGateway simulationIndexClientGateway;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @Autowired
  private IndexedSimEventListener indexedSimEventListener;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @BeforeEach
  void init() {
    simpleElasticTestClient.createDefaultIndexTemplate(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME, PRODUCTION_ELASTIC_READ_ALIAS_NAME);

    indexedEventListener.clear();
    indexedSimEventListener.clear();
  }

  @AfterEach
  void cleanup() {
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
    simpleElasticTestClient.removeDefaultIndexTemplate();
    removeData();
  }

  @Test
  void shouldReturnConfirmationWhenProductionDataIndexRequested() {
    ProductionDataIndexRequest request = ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(of(ALERT_1))
        .setRequestId(randomUUID().toString())
        .build();

    productionIndexClientGateway.indexRequest(request);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent()).isPresent();
    assertThat(indexedEventListener.getLastEvent().get().getRequestId())
        .isEqualTo(request.getRequestId());

    var source = simpleElasticTestClient.getSource(PRODUCTION_ELASTIC_READ_ALIAS_NAME, DOCUMENT_ID);
    assertThat(source).isEqualTo(MAPPED_ALERT_1);
  }

  @Test
  void shouldReturnConfirmationWhenSimulationDataIndexRequested() {
    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .setAnalysisName(SIMULATION_ANALYSIS_NAME)
        .addAllAlerts(of(ALERT_SIM_1))
        .build();

    simulationIndexClientGateway.indexRequest(request);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedSimEventListener.hasAnyEvent());
    assertThat(indexedSimEventListener.getLastEvent()).isPresent();
    assertThat(indexedSimEventListener.getLastEvent().get().getRequestId())
        .isEqualTo(request.getRequestId());

    var source = simpleElasticTestClient.getSource(
        SIMULATION_ELASTIC_INDEX_NAME, ALERT_1.getName());
    assertThat(source).isEqualTo(MAPPED_ALERT_11);
  }

  @Test
  void shouldMergeMultipleRequestsForTheSameAlert() {
    sendProductionRequestWithPayload(DISCRIMINATOR_1, Values.ALERT_NAME, Map.of(
        SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP));
    sendProductionRequestWithPayload(DISCRIMINATOR_1, Values.ALERT_NAME, Map.of(
        SourceAlertKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP));

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAtLeastEventCount(2));

    var source = simpleElasticTestClient.getSource(PRODUCTION_ELASTIC_READ_ALIAS_NAME, DOCUMENT_ID);
    assertThat(source).containsAllEntriesOf(Map.of(
        MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
        MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP));
  }

  @Test
  void shouldMergeProductionDataWhenSimulationRequestIsSent() {
    sendProductionRequestWithPayload(DISCRIMINATOR_1, Values.ALERT_NAME, Map.of(
        SourceAlertKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP));
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAtLeastEventCount(1));

    sendSimulationRequestWithPayload(SIMULATION_ANALYSIS_NAME, Values.ALERT_NAME, Map.of(
        SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP));
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedSimEventListener.hasAtLeastEventCount(1));

    var source =
        simpleElasticTestClient.getSource(SIMULATION_ELASTIC_INDEX_NAME, Values.ALERT_NAME);
    assertThat(source).containsAllEntriesOf(Map.of(
        MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
        MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP));
  }

  private void sendProductionRequestWithPayload(
      String discriminator, String alertName, Map<String, String> payload) {

    Alert alert = Alert.newBuilder()
        .setDiscriminator(discriminator)
        .setName(alertName)
        .setPayload(convertMapToPayload(payload))
        .build();

    ProductionDataIndexRequest request = ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(of(alert))
        .setRequestId(randomUUID().toString())
        .build();

    productionIndexClientGateway.indexRequest(request);
  }

  private void sendSimulationRequestWithPayload(
      String analysisName, String alertName, Map<String, String> payload) {

    com.silenteight.data.api.v1.Alert alert = com.silenteight.data.api.v1.Alert.newBuilder()
        .setDiscriminator(alertName)
        .setName(alertName)
        .setPayload(convertMapToPayload(payload))
        .build();

    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .addAllAlerts(of(alert))
        .setAnalysisName(analysisName)
        .setRequestId(randomUUID().toString())
        .build();

    simulationIndexClientGateway.indexRequest(request);
  }

  private void removeData() {
    safeDeleteIndex(SIMULATION_ELASTIC_INDEX_NAME);
    safeDeleteIndex(PRODUCTION_ELASTIC_WRITE_INDEX_NAME);
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }
}
