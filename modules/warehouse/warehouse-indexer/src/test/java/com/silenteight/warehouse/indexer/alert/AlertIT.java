package com.silenteight.warehouse.indexer.alert;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_1;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_2;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITHOUT_MATCHES;
import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = AlertTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
@Slf4j
class AlertIT {

  private static final String PRODUCTION_WRITE_INDEX_NAME = PRODUCTION_ELASTIC_INDEX_NAME + ".new";
  private static final String PRODUCTION_READ_ALIAS_NAME = PRODUCTION_ELASTIC_INDEX_NAME;
  private static final WriteIndexResolver INDEX_RESOLVER =
      new FixedIndexedResolver(PRODUCTION_WRITE_INDEX_NAME);

  @Autowired
  private AlertIndexService underTest;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @BeforeEach
  void init() {
    simpleElasticTestClient.createIndexTemplate(
        PRODUCTION_WRITE_INDEX_NAME, PRODUCTION_READ_ALIAS_NAME);
  }

  @AfterEach
  void cleanup() {
    simpleElasticTestClient.removeIndexTemplate();
    safeDeleteIndex(PRODUCTION_WRITE_INDEX_NAME);
  }

  @Test
  void shouldIgnoreMissingMatches() {
    underTest.indexAlerts(of(ALERT_WITHOUT_MATCHES), INDEX_RESOLVER);

    await()
        .atMost(5, SECONDS)
        .until(() -> simpleElasticTestClient.getDocumentCount(PRODUCTION_READ_ALIAS_NAME) > 0);

    long documentCount = simpleElasticTestClient.getDocumentCount(PRODUCTION_READ_ALIAS_NAME);
    assertThat(documentCount).isEqualTo(1);
  }

  @Test
  void shouldCreateSingleDocumentForEachAlert() {
    List<Alert> alerts = of(ALERT_1, ALERT_2);
    underTest.indexAlerts(alerts, INDEX_RESOLVER);

    await()
        .atMost(5, SECONDS)
        .until(() -> simpleElasticTestClient.getDocumentCount(PRODUCTION_READ_ALIAS_NAME) > 0);

    long documentCount = simpleElasticTestClient.getDocumentCount(PRODUCTION_READ_ALIAS_NAME);
    assertThat(documentCount).isEqualTo(alerts.size());
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }
}
