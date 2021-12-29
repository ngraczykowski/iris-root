package com.silenteight.warehouse.retention.simulation;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants;
import com.silenteight.warehouse.test.client.gateway.AnalysisExpiredClientGateway;
import com.silenteight.warehouse.test.client.listener.sim.IndexedSimEventListener;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.Callable;

import static com.silenteight.warehouse.retention.simulation.RetentionSimulationFixtures.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = RetentionSimulationTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    OpendistroElasticContainerInitializer.class
})
class AnalysisExpiredIT {

  private static final String EMPTY_STRING = "";

  @Autowired
  private AnalysisExpiredClientGateway analysisExpiredClientGateway;
  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;
  @Autowired
  private IndexedSimEventListener indexedSimEventListener;

  @AfterEach
  void cleanUp() {
    indexedSimEventListener.clear();
    safeDeleteIndex(SIMULATION_ELASTIC_INDEX_NAME);
  }

  @SneakyThrows
  @Test
  void shouldRemoveAnalysisWhenAnalysisExpiredReceived() {
    // given
    simpleElasticTestClient.storeData(
        SIMULATION_ELASTIC_INDEX_NAME, DISCRIMINATOR_1, MAPPED_ALERT_1);

    // when
    analysisExpiredClientGateway.indexRequest(ANALYSIS_EXPIRED_REQUEST);
    await()
        .atMost(5, SECONDS)
        .until(isDocumentUpdated(DISCRIMINATOR_1));

    // then
    var document =
        simpleElasticTestClient.getSource(SIMULATION_ELASTIC_INDEX_NAME, DISCRIMINATOR_1);

    assertThat(document.get(RECOMMENDATION_COMMENT_PREFIXED)).isEqualTo(EMPTY_STRING);
    assertThat(document.get(ANALYST_COMMENT_PREFIXED)).isEqualTo(EMPTY_STRING);
    assertThat(document.get(AlertMapperConstants.ALERT_NAME)).isEqualTo(Values.ALERT_NAME);
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }

  private Callable<Boolean> isDocumentUpdated(String discriminator) {
    return () -> simpleElasticTestClient
        .isDocUpdated(SIMULATION_ELASTIC_INDEX_NAME, discriminator, 1);
  }
}
