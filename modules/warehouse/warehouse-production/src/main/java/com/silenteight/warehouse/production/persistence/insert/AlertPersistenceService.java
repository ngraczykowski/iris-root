package com.silenteight.warehouse.production.persistence.insert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.production.persistence.mapping.alert.AlertDefinition;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.sql.Timestamp.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class AlertPersistenceService {

  private static final String DISCRIMINATOR_PARAMETER = "discriminator";
  private static final String ALERT_NAME_PARAMETER = "name";
  private static final String RECOMMENDATION_DATE_PARAMETER = "recommendationDate";
  private static final String PAYLOAD_PARAMETER = "payload";

  private static final String INSERT_ALERT_SQL =
      "INSERT INTO warehouse_alert(discriminator, name, recommendation_date, payload)"
          + " VALUES (:discriminator,:name,:recommendationDate,TO_JSON(:payload::json))"
          + " ON CONFLICT (discriminator) "
          + " DO UPDATE SET name = excluded.name,"
          + " payload = warehouse_alert.payload || excluded.payload,"
          + " recommendation_date ="
          + " coalesce(warehouse_alert.recommendation_date, excluded.recommendation_date)"
          + " RETURNING id AS map_id";

  long persist(NamedParameterJdbcTemplate jdbcTemplate, AlertDefinition alertDefinition) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    MapSqlParameterSource parameters = toSqlParameters(alertDefinition);
    jdbcTemplate.update(INSERT_ALERT_SQL, parameters, keyHolder);
    return getPersistedAlertId(keyHolder, alertDefinition.getDiscriminator());
  }

  private static MapSqlParameterSource toSqlParameters(AlertDefinition alertDefinition) {
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue(DISCRIMINATOR_PARAMETER, alertDefinition.getDiscriminator());
    parameters.addValue(ALERT_NAME_PARAMETER, alertDefinition.getName());
    parameters.addValue(PAYLOAD_PARAMETER, alertDefinition.getPayload());
    parameters.addValue(
        RECOMMENDATION_DATE_PARAMETER,
        getRecommendationDateAsTimestamp(alertDefinition.getRecommendationDate()));

    return parameters;
  }

  private static Timestamp getRecommendationDateAsTimestamp(OffsetDateTime recommendationDate) {
    if (recommendationDate == null)
      return null;

    return valueOf(recommendationDate.atZoneSameInstant(UTC).toLocalDateTime());
  }

  private static long getPersistedAlertId(KeyHolder keyHolder, String discriminator) {
    return ofNullable(keyHolder.getKey())
        .map(Number::longValue)
        .orElseThrow(() -> new AlertNotPersistedException(discriminator));
  }
}
