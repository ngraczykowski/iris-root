package com.silenteight.warehouse.indexer;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.prod.IndexedEventListener;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.indexer.IndexerFixtures.ALERT_1;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_READ_ALIAS_NAME;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_WRITE_INDEX_NAME;
import static com.silenteight.warehouse.indexer.IndexerFixtures.SIMULATION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
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
@DirtiesContext
class IndexerIT {

  private static final int TIMEOUT = 5;

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @BeforeEach
  void init() {
    simpleElasticTestClient.createDefaultIndexTemplate(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME, PRODUCTION_ELASTIC_READ_ALIAS_NAME);

    indexedEventListener.clear();
  }

  @AfterEach
  void cleanup() {
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
    assertThat(indexedEventListener.getLastEventId()).hasValue(request.getRequestId());

    var source = simpleElasticTestClient.getSource(PRODUCTION_ELASTIC_READ_ALIAS_NAME, DOCUMENT_ID);
    assertThat(source).isEqualTo(MAPPED_ALERT_1);
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
