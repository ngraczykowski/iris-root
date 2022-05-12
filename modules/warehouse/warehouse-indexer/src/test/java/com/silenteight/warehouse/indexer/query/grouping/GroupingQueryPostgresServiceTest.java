package com.silenteight.warehouse.indexer.query.grouping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.removeAlertPrefix;

class GroupingQueryPostgresServiceTest extends GroupingQueryServiceTest {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  @Language("PostgreSQL")
  private static final String DELETE_ALERTS_SQL = "DELETE FROM warehouse_alert;";
  @Language("PostgreSQL")
  private static final String INSERT_TEMPLATE =
      "INSERT INTO warehouse_alert(id, discriminator, name, created_at, recommendation_date,"
          + " payload) VALUES(%s, '%s', '%s', '%s', '%s', '%s')";

  @Autowired
  @Qualifier("postgres")
  GroupingQueryService service;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @AfterEach
  void cleanup() {
    jdbcTemplate.execute(DELETE_ALERTS_SQL);
  }

  @Override
  protected GroupingQueryService provideServiceForTest() {
    return service;
  }

  @Override
  protected void insertRow(
      String id, Map<String, Object> alertMapping, String processingTimeStamp, String alertName) {
    jdbcTemplate.execute(
        prepareInsertString(ThreadLocalRandom.current().nextInt(1, 10000), id, alertName,
            processingTimeStamp, createJsonFromMap(alertMapping)));
  }

  private String prepareInsertString(
      int id, String name, String discriminator, String timestamp, ObjectNode payload) {
    return String.format(
        INSERT_TEMPLATE, id, name, discriminator, timestamp, timestamp, payload.toString());
  }

  private ObjectNode createJsonFromMap(Map<String, Object> map) {
    ObjectNode json = OBJECT_MAPPER.createObjectNode();
    map.forEach((key, value) -> json.put(removeAlertPrefix(key), value.toString()));
    return json;
  }
}
