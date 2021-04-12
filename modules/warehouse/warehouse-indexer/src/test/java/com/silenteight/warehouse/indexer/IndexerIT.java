package com.silenteight.warehouse.indexer;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.sep.base.testing.BaseIntegrationTest;
import com.silenteight.warehouse.indexer.indextestclient.gateway.IndexClientGateway;
import com.silenteight.warehouse.indexer.indextestclient.listener.IndexedEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.DATA_INDEX_REQUEST_WITH_ALERTS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = IndexerTestConfiguration.class)
class IndexerIT extends BaseIntegrationTest {


  @BeforeEach
  void init() {
    indexedEventListener.clear();
  }

  @Autowired
  IndexClientGateway indexClientGateway;

  @Autowired
  IndexedEventListener indexedEventListener;

  @Disabled("Requires Elasticsearch")
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
  }
}
