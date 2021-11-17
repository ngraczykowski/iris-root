package com.silenteight.warehouse.retention.production.personalinformation;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.test.client.gateway.PersonalInformationExpiredClientGateway;
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

import static com.silenteight.warehouse.retention.production.personalinformation.PersonalInformationExpiredFixtures.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
class PersonalInformationExpiredIT {

  private static final String EMPTY_STRING = "";
  private static final int TIMEOUT = 5;

  @Autowired
  private PersonalInformationExpiredClientGateway personalInformationExpiredClientGateway;
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
  void shouldErasePiiDataWhenPersonalInformationExpiredRequested() {
    //given
    productionIndexClientGateway.indexRequest(PRODUCTION_DATA_INDEX_REQUEST_1);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());

    //when
    personalInformationExpiredClientGateway.indexRequest(PERSONAL_INFORMATION_EXPIRED_REQUEST);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> {
          var document = simpleElasticTestClient.getSource(
              PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_1);
          return !document.isEmpty() &&
              document.getOrDefault(RECOMMENDATION_COMMENT_PREFIXED, COMMENT_TP)
                  .equals(EMPTY_STRING);
        });

    //then
    var storedAlert = simpleElasticTestClient.getSource(
        PRODUCTION_ALERT_ELASTIC_READ_ALIAS_NAME, DISCRIMINATOR_1);
    var storedMatch = simpleElasticTestClient.getSource(
        PRODUCTION_MATCH_ELASTIC_READ_ALIAS_NAME, MATCH_DISCRIMINATOR_1);
    assertThat(storedAlert.get(RECOMMENDATION_COMMENT_PREFIXED)).isEqualTo(EMPTY_STRING);
    assertThat(storedAlert.get(ANALYST_COMMENT_PREFIXED)).isEqualTo(EMPTY_STRING);
    assertThat(storedMatch.get(RECOMMENDATION_COMMENT_PREFIXED)).isEqualTo(EMPTY_STRING);
    assertThat(storedMatch.get(ANALYST_COMMENT_PREFIXED)).isEqualTo(EMPTY_STRING);
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }
}
