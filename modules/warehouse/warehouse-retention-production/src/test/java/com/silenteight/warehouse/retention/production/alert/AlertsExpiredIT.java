package com.silenteight.warehouse.retention.production.alert;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.retention.production.RetentionProductionTestConfiguration;
import com.silenteight.warehouse.test.client.gateway.AlertsExpiredClientGateway;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.prod.IndexedEventListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.retention.production.RetentionFixtures.ALERTS_EXPIRED_REQUEST;
import static com.silenteight.warehouse.retention.production.RetentionFixtures.PRODUCTION_DATA_INDEX_REQUEST_1;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = RetentionProductionTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class
})
@ActiveProfiles("jpa-test")
class AlertsExpiredIT {

  private static final int TIMEOUT = 5;
  private static final String ALERT_TABLE = "warehouse_alert";
  private static final String MATCH_TABLE = "warehouse_match";
  private static final String LABEL_TABLE = "warehouse_alert_label";
  private static final String SELECT_NAME_QUERY = "SELECT name FROM %s";

  @Autowired
  private AlertsExpiredClientGateway alertsExpiredClientGateway;
  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;
  @Autowired
  private IndexedEventListener indexedEventListener;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    indexedEventListener.clear();
  }

  @SneakyThrows
  @Test
  void shouldEraseAlertsWhenAlertsExpiredRequested() {
    //given
    productionIndexClientGateway.indexRequest(PRODUCTION_DATA_INDEX_REQUEST_1);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());

    assertThat(hasRecords(ALERT_TABLE)).isTrue();
    assertThat(hasRecords(MATCH_TABLE)).isTrue();
    assertThat(hasRecords(LABEL_TABLE)).isTrue();

    //when
    alertsExpiredClientGateway.indexRequest(ALERTS_EXPIRED_REQUEST);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> !hasRecords(ALERT_TABLE));

    //then
    assertThat(hasRecords(ALERT_TABLE)).isFalse();
    assertThat(hasRecords(MATCH_TABLE)).isFalse();
    assertThat(hasRecords(LABEL_TABLE)).isFalse();
  }

  private boolean hasRecords(String tableName) {
    String query = format(SELECT_NAME_QUERY, tableName);
    List<String> names = jdbcTemplate.queryForList(query, String.class);
    return names.size() > 0;
  }
}
