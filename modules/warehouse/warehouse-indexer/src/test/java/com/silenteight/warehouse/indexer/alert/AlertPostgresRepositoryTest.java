package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import com.google.common.collect.ImmutableListMultimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.silenteight.warehouse.indexer.alert.AlertColumnName.CREATED_AT;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertPostgresRepositoryTest {

  private static final Timestamp TIME_FROM = Timestamp.valueOf("2021-06-01 00:00:01.0");
  private static final Timestamp TIME_TO = Timestamp.valueOf("2021-06-30 00:00:01.0");
  private static final String TIME_FROM_STRING = "2021-06-01T00:00:01Z";
  private static final String TIME_TO_STRING = "2021-06-30T00:00:01Z";
  private static final int LIMIT = 2;
  private static final String TEST_1 =
      "SELECT * FROM warehouse_alert WHERE name IS NOT NULL AND created_at BETWEEN ? AND ?AND name"
          + " IN (?,?) ORDER BY RANDOM() LIMIT ?";
  private static final String TEST_2 =
      "SELECT * FROM warehouse_alert WHERE name IS NOT NULL AND created_at BETWEEN ? AND ? ORDER BY"
          + " RANDOM() LIMIT ?";
  private static final String TEST_3 =
      "SELECT * FROM warehouse_alert WHERE name IS NOT NULL AND created_at BETWEEN ? AND ?AND"
          + " payload->>? = ? AND payload->>? in (?,?) ORDER BY RANDOM() LIMIT ?";
  private static final String TEST_4 =
      "SELECT * FROM warehouse_alert WHERE name IS NOT NULL AND created_at BETWEEN ? AND ?AND name"
          + " IN (?,?)AND payload->>? = ? AND payload->>? in (?,?) ORDER BY RANDOM() LIMIT ?";
  private static final String TEST_5 =
      "SELECT payload ->> '%s' AS \"0\", payload ->> '%s' AS \"1\" FROM warehouse_alert "
          + "WHERE created_at BETWEEN ? AND ?AND payload->>? = ? AND payload->>? in (?,?)";

  @Mock
  private JdbcTemplate jdbcTemplateMock;

  private AlertDtoRowMapper alertDtoRowMapper = new AlertDtoRowMapper();

  private AlertPostgresRepository repository;

  @BeforeEach
  void setUp() {
    repository = new AlertPostgresRepository(jdbcTemplateMock, alertDtoRowMapper);
  }

  @Test
  void fetchWithoutRFilters_sqlWithoutAlertNaneAndWithoutAdditionalClauses() {
    // When
    repository = new AlertPostgresRepository(jdbcTemplateMock, alertDtoRowMapper);
    repository.fetchRandomAlerts(
        CREATED_AT, TIME_FROM_STRING, TIME_TO_STRING, LIMIT,
        ImmutableListMultimap.of(),
        List.of());

    // Then
    verify(jdbcTemplateMock).query(
        eq(TEST_2),
        eq(alertDtoRowMapper), eq(TIME_FROM),
        eq(TIME_TO), eq(LIMIT));
  }

  @Test
  void fetchWithoutFilters_sqlWithAlertNameAndWithoutAdditionalClauses() {
    // When
    repository.fetchRandomAlerts(
        CREATED_AT, TIME_FROM_STRING, TIME_TO_STRING, LIMIT,
        ImmutableListMultimap.of(), List.of("alertName", "alertName2"));

    // Then
    verify(jdbcTemplateMock).query(
        eq(TEST_1),
        eq(alertDtoRowMapper), eq(TIME_FROM),
        eq(TIME_TO), eq("alertName"), eq("alertName2"), eq(LIMIT));
  }

  @Test
  void fetchWithFilters_sqlContainsAdditionalClauses() {
    // When
    repository.fetchRandomAlerts(
        CREATED_AT, TIME_FROM_STRING, TIME_TO_STRING, LIMIT,
        ImmutableListMultimap.of(
            "propertyName", List.of("value1"), "propertyName2",
            List.of("value1", "value2")), List.of());

    // Then
    verify(jdbcTemplateMock).query(
        eq(TEST_3),
        eq(alertDtoRowMapper), eq(TIME_FROM),
        eq(TIME_TO), eq("propertyName"), eq("value1"), eq("propertyName2"),
        eq("value1"), eq("value2"), eq(LIMIT));
  }

  @Test
  void fetchWithFilters_sqlContainsAlertNameAndAdditionalClauses() {
    // When
    repository.fetchRandomAlerts(
        CREATED_AT, TIME_FROM_STRING, TIME_TO_STRING, LIMIT,
        ImmutableListMultimap.of(
            "propertyName", List.of("value1"), "propertyName2",
            List.of("value1", "value2")), List.of("alertName", "alertName2"));

    // Then
    verify(jdbcTemplateMock).query(
        eq(TEST_4),
        eq(alertDtoRowMapper), eq(TIME_FROM),
        eq(TIME_TO), eq("alertName"), eq("alertName2"), eq("propertyName"),
        eq("value1"), eq("propertyName2"), eq("value1"), eq("value2"),
        eq(LIMIT));

  }

  @Test
  void fetchGroupedByWithFilters_sqlContainsFiltersAndGroupByFields() {
    String givenGroupName = "group1";
    String givenGroupName2 = "group2";
    // When
    repository.fetchGroupedAlerts(
        CREATED_AT,
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(TIME_FROM.getTime()), ZoneId.of("UTC")),
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(TIME_TO.getTime()), ZoneId.of("UTC")),
        ImmutableListMultimap.of(
            "propertyName", List.of("value1"), "propertyName2",
            List.of("value1", "value2")), List.of(givenGroupName, givenGroupName2));

    // Then
    verify(jdbcTemplateMock).query(
        eq(String.format(TEST_5, givenGroupName, givenGroupName2)),
        ArgumentMatchers.<RowMapper<AlertDto>>any(), any(),
        any(), eq("propertyName"),
        eq("value1"), eq("propertyName2"), eq("value1"), eq("value2"));
  }
}
