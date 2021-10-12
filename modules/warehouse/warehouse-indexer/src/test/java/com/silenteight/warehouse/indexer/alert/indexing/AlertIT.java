package com.silenteight.warehouse.indexer.alert.indexing;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_READ_ALIAS_NAME;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_WRITE_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = AlertTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
@Slf4j
class AlertIT {

  @Autowired
  private AlertIndexService underTest;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @BeforeEach
  void init() {
    simpleElasticTestClient.createIndexTemplate(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME, PRODUCTION_ELASTIC_READ_ALIAS_NAME);
  }

  @AfterEach
  void cleanup() {
    simpleElasticTestClient.removeIndexTemplate();
    safeDeleteIndex(PRODUCTION_ELASTIC_WRITE_INDEX_NAME);
  }

  @Test
  void shouldStoreAlerts() {
    MapWithIndex mapWithIndex = new MapWithIndex(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME,
        DOCUMENT_ID,
        Map.of(MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP));

    underTest.saveAlerts(List.of(mapWithIndex));

    await()
        .atMost(5, SECONDS)
        .until(
            () -> simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_READ_ALIAS_NAME) > 0);

    long documentCount =
        simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_READ_ALIAS_NAME);
    assertThat(documentCount).isEqualTo(1);
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }
}
