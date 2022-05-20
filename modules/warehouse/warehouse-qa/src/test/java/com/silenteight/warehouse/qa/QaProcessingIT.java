package com.silenteight.warehouse.qa;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaAlert.State;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.warehouse.common.testing.e2e.CleanDatabase;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.gateway.QaIndexClientGateway;
import com.silenteight.warehouse.test.client.listener.prod.IndexedEventListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.ResultSet;
import java.util.Map;

import static com.silenteight.warehouse.qa.QaProcessingTestFixtures.*;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = QaProcessingConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(
    initializers = {
        RabbitTestInitializer.class,
        PostgresTestInitializer.class })
@ActiveProfiles("jpa-test")
@CleanDatabase
class QaProcessingIT {

  private static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE
      = new TypeReference<>() {};
  private static final String QA_LEVEL_0_STATE = "qa.level-0.state";
  private static final String QA_LEVEL_0_COMMENT = "qa.level-0.comment";
  private static final String QA_LEVEL_0_TIMESTAMP = "qa.level-0.NEW.timestamp";
  private static final int TIMEOUT = 5;

  @Autowired
  private QaIndexClientGateway qaIndexClientGateway;

  @Autowired
  private ProductionIndexClientGateway productionIndexClientGateway;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private IndexedEventListener indexedEventListener;

  @Test
  void shouldProcessQaRequest() {

    // given
    productionIndexClientGateway.indexRequest(PRODUCTION_REQUEST_V1);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> indexedEventListener.hasAnyEvent());

    // when
    qaIndexClientGateway.indexRequest(QA_REQUEST);
    await()
        .atMost(TIMEOUT, SECONDS)
        .until(() -> alertHasQaFields(ALERT_NAME));

    // then
    Map<String, String> payload = getPayload(ALERT_NAME);
    assertThat(payload)
        .containsEntry("name", ALERT_NAME)
        .containsEntry(QA_LEVEL_0_STATE, State.NEW.toString())
        .containsEntry(QA_LEVEL_0_COMMENT, QA_COMMENT)
        .containsEntry(QA_LEVEL_0_TIMESTAMP, PROCESSING_TIMESTAMP);
  }

  private boolean alertHasQaFields(String alertName) {
    Map<String, String> payload = getPayload(alertName);
    return payload.containsKey(QA_LEVEL_0_STATE);
  }

  private Map<String, String> getPayload(String alertName) {
    String sqlGetPayloadByName = "SELECT payload FROM warehouse_alert WHERE name = :alertName";
    Map<String, String> parameters = Map.of("alertName", alertName);
    return jdbcTemplate.query(sqlGetPayloadByName, parameters, new PayloadRowMapper()).stream()
        .findFirst()
        .orElse(emptyMap());
  }

  class PayloadRowMapper implements RowMapper<Map<String, String>> {

    @SneakyThrows
    @Override
    public Map<String, String> mapRow(ResultSet rs, int rowNum) {
      String payload = rs.getString("payload");
      return objectMapper.readValue(payload, MAP_TYPE_REFERENCE);
    }
  }
}
