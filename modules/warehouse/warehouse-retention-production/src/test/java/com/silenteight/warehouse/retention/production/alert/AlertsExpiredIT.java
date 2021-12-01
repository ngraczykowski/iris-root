package com.silenteight.warehouse.retention.production.alert;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.retention.production.RetentionProductionTestConfiguration;
import com.silenteight.warehouse.test.client.gateway.AlertsExpiredClientGateway;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.prod.IndexedEventListener;

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

import static com.silenteight.warehouse.retention.production.RetentionFixtures.*;
import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = RetentionProductionTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    OpendistroElasticContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
class AlertsExpiredIT {

  private static final int TIMEOUT = 5;

  @Autowired
  private AlertsExpiredClientGateway alertsExpiredClientGateway;
  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;
  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;
  @Autowired
  private IndexedEventListener indexedEventListener;

  @BeforeEach
  void setUp() {
    simpleElasticTestClient.createIndexTemplate(
        PRODUCTION_ALERT_ELASTIC_WRITE_INDEX_NAME,
        PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME,
        ALERT_ELASTIC_TEMPLATE_NAME);
    simpleElasticTestClient.createIndexTemplate(
        PRODUCTION_MATCH_ELASTIC_WRITE_INDEX_NAME,
        PRODUCTION_MATCH_ELASTIC_READ_ALIAS_NAME,
        MATCH_ELASTIC_TEMPLATE_NAME);
    indexedEventListener.clear();
  }

  @AfterEach
  void cleanUp() {
    safeDeleteIndex(PRODUCTION_ALERT_ELASTIC_WRITE_INDEX_NAME);
    safeDeleteIndex(PRODUCTION_MATCH_ELASTIC_WRITE_INDEX_NAME);
    simpleElasticTestClient.removeIndexTemplate(ALERT_ELASTIC_TEMPLATE_NAME);
    simpleElasticTestClient.removeIndexTemplate(MATCH_ELASTIC_TEMPLATE_NAME);
  }

  @SneakyThrows
  @Test
  void shouldEraseAlertsWhenAlertsExpiredRequested() {
    //given
    productionIndexClientGateway.indexRequest(PRODUCTION_DATA_INDEX_REQUEST_2);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());

    assertThat(simpleElasticTestClient.getSource(
        PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_1)).isNotNull();

    assertThat(simpleElasticTestClient.getSource(
        PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_2)).isNotNull();

    assertThat(simpleElasticTestClient.getSource(
        PRODUCTION_MATCH_ELASTIC_READ_ALIAS_NAME, MATCH_DISCRIMINATOR_1)).isNotNull();

    //when
    alertsExpiredClientGateway.indexRequest(ALERTS_EXPIRED_REQUEST);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> isNull(simpleElasticTestClient.getSource(
            PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_1)));

    //then
    assertThat(simpleElasticTestClient.getSource(
        PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_1)).isNull();

    assertThat(simpleElasticTestClient.getSource(
        PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_2)).isNull();

    assertThat(simpleElasticTestClient.getSource(
        PRODUCTION_MATCH_ELASTIC_READ_ALIAS_NAME, MATCH_DISCRIMINATOR_1)).isNull();
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }
}
