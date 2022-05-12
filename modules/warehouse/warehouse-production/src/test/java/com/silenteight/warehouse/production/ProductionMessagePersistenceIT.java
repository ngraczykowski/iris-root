package com.silenteight.warehouse.production;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.production.handler.ProductionRequestV2CommandHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.production.ProductionMessagePersistenceTestFixtures.*;
import static java.sql.Timestamp.valueOf;
import static java.time.OffsetDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = ProductionMessagePersistenceTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@ActiveProfiles("jpa-test")
@Transactional
class ProductionMessagePersistenceIT {

  private static final String NAME_COLUMN = "name";
  private static final String ALERT_ID_COLUMN = "alert_id";
  private static final String VALUE_COLUMN = "value";
  private static final String RECOMMENDATION_DATE_COLUMN = "recommendation_date";

  @Autowired
  private ProductionRequestV2CommandHandler v2CommandHandler;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldSaveOneEmptyAlertAndOneAlertWithTwoMatchesAndTwoLabels() {
    // when
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_1);

    // then
    List<String> alertNames = fetchAlertNames();
    assertThat(alertNames).containsOnly(ALERT_NAME_1, ALERT_NAME_2);

    List<Timestamp> recommendationDates = fetchRecommendationDates();
    Timestamp expectedTimestamp = getRecommendationDateAsTimestamp(RECOMMENDATION_DATE_VALUE_1);
    assertThat(recommendationDates).containsOnly(expectedTimestamp);

    List<String> matchNames = fetchMatchNames();
    assertThat(matchNames).containsOnly(MATCH_NAME_1, MATCH_NAME_2);

    int alertId = fetchAlertIdForName(ALERT_NAME_1);

    List<Integer> alertIdFromMatches = fetchAlertIdFromMatches();
    assertThat(alertIdFromMatches).containsOnly(alertId);

    List<String> labelValues = fetchLabelValues();
    assertThat(labelValues).containsOnly(RISK_TYPE_VALUE, LOB_COUNTRY_VALUE_1);

    List<Integer> alertIdFromLabels = fetchAlertIdFromLabels();
    assertThat(alertIdFromLabels).containsOnly(alertId);
  }

  @Test
  void shouldSaveEmptyAlert() {
    // when
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_2);

    // then
    List<String> alertNames = fetchAlertNames();
    assertThat(alertNames).contains(ALERT_NAME_2);

    List<Timestamp> recommendationDates = fetchRecommendationDates();
    Timestamp expectedTimestamp = getRecommendationDateAsTimestamp(RECOMMENDATION_DATE_VALUE_1);
    assertThat(recommendationDates).containsOnlyOnce(expectedTimestamp);

    List<String> matchNames = fetchMatchNames();
    assertThat(matchNames).isEmpty();

    List<Integer> alertIdFromMatches = fetchAlertIdFromMatches();
    assertThat(alertIdFromMatches).isEmpty();

    List<String> labelValues = fetchLabelValues();
    assertThat(labelValues).isEmpty();

    List<Integer> alertIdFromLabels = fetchAlertIdFromLabels();
    assertThat(alertIdFromLabels).isEmpty();
  }

  @Test
  void shouldSaveAlertWithoutRecommendationDate() {
    // when
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_3);

    // then
    List<String> alertNames = fetchAlertNames();
    assertThat(alertNames).contains(ALERT_NAME_3);

    List<Timestamp> recommendationDates = fetchRecommendationDates();
    assertThat(recommendationDates).containsOnlyNulls();

    List<String> matchNames = fetchMatchNames();
    assertThat(matchNames).isEmpty();

    List<Integer> alertIdFromMatches = fetchAlertIdFromMatches();
    assertThat(alertIdFromMatches).isEmpty();

    List<String> labelValues = fetchLabelValues();
    assertThat(labelValues).isEmpty();

    List<Integer> alertIdFromLabels = fetchAlertIdFromLabels();
    assertThat(alertIdFromLabels).isEmpty();
  }

  @Test
  void shouldUpdateAlertMatchAndLabels() {
    // when
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_4);
    v2CommandHandler.handle(UPDATED_PRODUCTION_DATA_INDEX_REQUEST_4);

    // then
    List<String> alertNames = fetchAlertNames();
    assertThat(alertNames).containsOnly(ALERT_NAME_1);

    List<Timestamp> recommendationDates = fetchRecommendationDates();
    Timestamp expectedTimestamp = getRecommendationDateAsTimestamp(RECOMMENDATION_DATE_VALUE_1);
    assertThat(recommendationDates).containsOnlyOnce(expectedTimestamp);

    List<String> matchNames = fetchMatchNames();
    assertThat(matchNames).containsOnly(MATCH_NAME_1);

    int alertId = fetchAlertIdForName(ALERT_NAME_1);

    List<Integer> alertIdFromMatches = fetchAlertIdFromMatches();
    assertThat(alertIdFromMatches).containsOnly(alertId);

    List<String> labelValues = fetchLabelValues();
    assertThat(labelValues).containsOnly(
        RISK_TYPE_VALUE, LOB_COUNTRY_VALUE_2, ALERT_ANALYST_DECISION_VALUE);

    List<Integer> alertIdFromLabels = fetchAlertIdFromLabels();
    assertThat(alertIdFromLabels).containsOnly(alertId);
  }

  @Test
  void shouldUpdateRecommendationDateWhenAlreadyPersistedIsNull() {
    // given
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_5);
    List<Timestamp> recommendationDates = fetchRecommendationDates();
    assertThat(recommendationDates).containsOnlyNulls();

    // when
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_4);

    // then
    recommendationDates = fetchRecommendationDates();
    Timestamp expectedTimestamp = getRecommendationDateAsTimestamp(RECOMMENDATION_DATE_VALUE_1);
    assertThat(recommendationDates).containsOnlyOnce(expectedTimestamp);
  }

  @Test
  void shouldNotUpdateRecommendationDateWhenAlreadyPersistedIsNotNull() {
    // given
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_4);
    List<Timestamp> recommendationDates = fetchRecommendationDates();
    Timestamp expectedTimestamp = getRecommendationDateAsTimestamp(RECOMMENDATION_DATE_VALUE_1);
    assertThat(recommendationDates).containsOnlyOnce(expectedTimestamp);

    // when
    v2CommandHandler.handle(PRODUCTION_DATA_INDEX_REQUEST_5);

    // then
    recommendationDates = fetchRecommendationDates();
    assertThat(recommendationDates).containsOnlyOnce(expectedTimestamp);
  }

  private int fetchAlertIdForName(String alertName) {
    return ofNullable(jdbcTemplate.queryForObject(
        "SELECT id FROM warehouse_alert WHERE name = ?", Integer.class, alertName))
        .orElseThrow();
  }

  private List<String> fetchAlertNames() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_alert",
        (rs, rowNum) -> rs.getString(NAME_COLUMN));
  }

  private List<String> fetchMatchNames() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_match",
        (rs, rowNum) -> rs.getString(NAME_COLUMN));
  }

  private List<Integer> fetchAlertIdFromMatches() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_match",
        (rs, rowNum) -> rs.getInt(ALERT_ID_COLUMN));
  }

  private List<String> fetchLabelValues() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_alert_label",
        (rs, rowNum) -> rs.getString(VALUE_COLUMN));
  }

  private List<Integer> fetchAlertIdFromLabels() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_alert_label",
        (rs, rowNum) -> rs.getInt(ALERT_ID_COLUMN));
  }

  private List<Timestamp> fetchRecommendationDates() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_alert",
        (rs, rowNum) -> rs.getTimestamp(RECOMMENDATION_DATE_COLUMN));
  }

  private Timestamp getRecommendationDateAsTimestamp(String recommendationDateValue) {
    OffsetDateTime recommendationDate = parse(recommendationDateValue);
    return valueOf(recommendationDate.atZoneSameInstant(UTC).toLocalDateTime());
  }
}
