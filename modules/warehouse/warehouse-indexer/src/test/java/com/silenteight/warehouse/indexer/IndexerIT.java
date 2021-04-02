package com.silenteight.warehouse.indexer;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.sep.base.testing.BaseIntegrationTest;
import com.silenteight.warehouse.indexer.indextestclient.gateway.IndexClientGateway;
import com.silenteight.warehouse.indexer.indextestclient.listener.IndexedEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = IndexerTestConfiguration.class)
class IndexerIT extends BaseIntegrationTest {

  private static final String REQUEST_ID = "TEST_123";

  @BeforeEach
  void init() {
    indexedEventListener.clear();
  }

  @Autowired
  IndexClientGateway indexClientGateway;

  @Autowired
  IndexedEventListener indexedEventListener;

  @Test
  void shouldReturnConfirmationWhenDataIndexRequested() {
    DataIndexRequest request = DataIndexRequest.newBuilder()
        .setRequestId(REQUEST_ID)
        .build();

    indexClientGateway.indexRequest(request);

    await()
        .atMost(5, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());
    assertThat(indexedEventListener.getLastEvent())
        .get()
        .extracting(DataIndexResponse::getRequestId)
        .isEqualTo(REQUEST_ID);
  }
}
