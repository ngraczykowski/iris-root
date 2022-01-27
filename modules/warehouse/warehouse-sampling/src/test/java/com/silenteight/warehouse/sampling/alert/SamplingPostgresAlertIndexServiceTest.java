package com.silenteight.warehouse.sampling.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static com.silenteight.warehouse.sampling.alert.SamplingTestFixtures.*;

@TestPropertySource(properties = "warehouse.alert-provider.is-sql-support-enabled=true")
class SamplingPostgresAlertIndexServiceTest extends SamplingAlertIndexService {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  @Language("PostgreSQL")
  private static final String DELETE_ALERTS_SQL = "DELETE FROM warehouse_alert;";
  private static final String INSERT_TEMPLATE =
      "INSERT INTO warehouse_alert(id, discriminator, name, created_at, recommendation_date,"
          + " payload) VALUES(%s, '%s', '%s', '%s', '%s', '%s')";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  protected void removeData() {
    jdbcTemplate.execute(DELETE_ALERTS_SQL);
  }

  protected void initDateForCorrectSamplingNumber() {
    ImmutableList<String> sqls = ImmutableList.of(
        prepareInsertString(
            1, DISCRIMINATOR_1, ALERT_NAME_1, PROCESSING_TIMESTAMP_1,
            createJsonFromMap(ALERT_1_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            2, DISCRIMINATOR_2, ALERT_NAME_2, PROCESSING_TIMESTAMP_1,
            createJsonFromMap(ALERT_2_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            3, DISCRIMINATOR_3, ALERT_NAME_3, PROCESSING_TIMESTAMP_2,
            createJsonFromMap(ALERT_3_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            4, DISCRIMINATOR_4, ALERT_NAME_4, PROCESSING_TIMESTAMP_3,
            createJsonFromMap(ALERT_4_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            5, DISCRIMINATOR_5, ALERT_NAME_5, PROCESSING_TIMESTAMP_4,
            createJsonFromMap(ALERT_5_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            6, DISCRIMINATOR_6, ALERT_NAME_6, PROCESSING_TIMESTAMP_4,
            createJsonFromMap(ALERT_6_MAP_WITHOUT_PREFIX))
    );
    sqls.forEach(jdbcTemplate::update);
  }

  private String prepareInsertString(
      int id, String name, String discriminator, String timestamp, ObjectNode payload) {
    return String.format(
        INSERT_TEMPLATE, id, name, discriminator, timestamp, timestamp, payload.toString());
  }

  private ObjectNode createJsonFromMap(Map<String, Object> map) {
    ObjectNode json = OBJECT_MAPPER.createObjectNode();
    map.forEach((key, value) -> json.put(key, value.toString()));
    return json;
  }

  protected void initDateForIncludedFilters() {
    ImmutableList<String> sqls = ImmutableList.of(
        prepareInsertString(
            1, DISCRIMINATOR_4, ALERT_NAME_4, PROCESSING_TIMESTAMP_3,
            createJsonFromMap(ALERT_4_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            2, DISCRIMINATOR_5, ALERT_NAME_5, PROCESSING_TIMESTAMP_4,
            createJsonFromMap(ALERT_5_MAP_WITHOUT_PREFIX)),
        prepareInsertString(
            3, DISCRIMINATOR_6, ALERT_NAME_6, PROCESSING_TIMESTAMP_4,
            createJsonFromMap(ALERT_6_MAP_WITHOUT_PREFIX))
    );
    sqls.forEach(jdbcTemplate::update);
  }
}