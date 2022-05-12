package com.silenteight.warehouse.production;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.e2e.CleanDatabase;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.prod.IndexedEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.production.ProductionProcessingTestFixtures.PRODUCTION_REQUEST_V1;
import static com.silenteight.warehouse.production.ProductionProcessingTestFixtures.PRODUCTION_REQUEST_V2;
import static com.silenteight.warehouse.production.ProductionProcessingTestFixtures.REQUEST_ID_V1;
import static com.silenteight.warehouse.production.ProductionProcessingTestFixtures.REQUEST_ID_V2;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = ProductionProcessingConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(
    initializers = {
        RabbitTestInitializer.class,
        PostgresTestInitializer.class })
@ActiveProfiles("jpa-test")
@CleanDatabase
class ProductionProcessingIT {

  private static final int TIMEOUT = 5;

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @BeforeEach
  void init() {
    indexedEventListener.clear();
  }

  @Test
  void shouldProcessProductionRequestV1() {
    productionIndexClientGateway.indexRequest(PRODUCTION_REQUEST_V1);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());

    assertThat(indexedEventListener.getLastEventId()).hasValue(REQUEST_ID_V1);
  }

  @Test
  void shouldProcessProductionRequestV2() {
    productionIndexClientGateway.indexRequest(PRODUCTION_REQUEST_V2);

    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());

    assertThat(indexedEventListener.getLastEventId()).hasValue(REQUEST_ID_V2);
  }
}
