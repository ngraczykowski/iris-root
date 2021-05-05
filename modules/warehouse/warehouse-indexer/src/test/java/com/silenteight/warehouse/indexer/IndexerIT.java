package com.silenteight.warehouse.indexer;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.indextestclient.gateway.IndexClientGateway;
import com.silenteight.warehouse.indexer.indextestclient.listener.IndexedEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.DATA_INDEX_REQUEST_WITH_ALERTS;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ANALYSIS_ID;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = IndexerTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    OpendistroElasticContainerInitializer.class
})
class IndexerIT {

  @BeforeEach
  void init() {
    indexedEventListener.clear();
  }

  @Autowired
  private IndexClientGateway indexClientGateway;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Test
  void shouldReturnConfirmationWhenDataIndexRequested() {
    DataIndexRequest request = DATA_INDEX_REQUEST_WITH_ALERTS;

    indexClientGateway.indexRequest(request);

    await()
        .atMost(5, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent())
        .get()
        .extracting(DataIndexResponse::getRequestId)
        .isEqualTo(request.getRequestId());

    var source = simpleElasticTestClient.getSource(ANALYSIS_ID, DOCUMENT_ID);
    assertThat(source).isEqualTo(ALERT_WITH_MATCHES_1_MAP);
  }
}
