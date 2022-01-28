package com.silenteight.warehouse.indexer.alert;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.StringUtils;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
public final class AlertPostgresRepository implements AlertRepository {

  @Language("PostgreSQL")
  private static final String FETCH =
      "SELECT * FROM warehouse_alert WHERE %s BETWEEN ? AND ?%s%s ORDER BY RANDOM() LIMIT ?";

  private static final String ALERT_NAME_FILTER = "AND name IN (%s)";
  private static final String JDBC_TEMPLATE_PARAMETER_INDICATOR = "?,";
  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  @Valid
  @NonNull
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<AlertDto> fetchRandomAlerts(
      AlertColumnName alertColumnName, String timeFrom, String timeTo, int limit,
      ListMultimap<String, List<String>> filters, List<String> alertNames) {

    String sql =
        String.format(
            FETCH, alertColumnName.getName(), getAlertNameFilter(alertNames),
            getPayloadFilters(filters));

    List<Object> jdbcParameters =
        createJdbcParameters(timeFrom, timeTo, limit, filters, alertNames);

    log.debug("Querying jdbctemplate with sql {} with parameters {}", sql, jdbcParameters);
    return jdbcTemplate.query(sql, (rs, rowNum) -> getAlertDto(rs),
        jdbcParameters.toArray(new Object[0]));
  }

  private static List<Object> createJdbcParameters(
      String timeFrom, String timeTo, int limit, ListMultimap<String, List<String>> filters,
      List<String> alertNames) {
    ImmutableList.Builder<Object> paramsBuilder = new ImmutableList.Builder<>();
    // Temporary we have to switch string again to timestamp to support ES and postgres at the
    // same time when ES is deprecated we can use timestamp instead of string as method parameter
    paramsBuilder.add(convertTimeToDate(timeFrom));
    paramsBuilder.add(convertTimeToDate(timeTo));
    paramsBuilder.addAll(alertNames);
    filters.entries().forEach((entry -> {
      paramsBuilder.add(entry.getKey());
      paramsBuilder.addAll(entry.getValue());
    }));
    paramsBuilder.add(limit);
    return paramsBuilder.build();
  }

  private static Timestamp convertTimeToDate(String date) {
    DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(date));
    return Timestamp.valueOf(localDateTime);
  }

  private static String getAlertNameFilter(List<String> alertNames) {
    if (alertNames.isEmpty()) {
      return "";
    }
    return String.format(
        ALERT_NAME_FILTER, createJdbTemplateIndicators(alertNames.size()));
  }

  private static String createJdbTemplateIndicators(int indicatorsCount) {
    return StringUtils.chop(JDBC_TEMPLATE_PARAMETER_INDICATOR.repeat(indicatorsCount));
  }

  private static String getPayloadFilters(ListMultimap<String, List<String>> filters) {
    if (filters.isEmpty()) {
      return "";
    }
    return filters
        .entries()
        .stream()
        .map(entry -> createSinglePayloadFilter(entry.getValue()))
        .collect(Collectors.joining("AND ", "AND ", ""));
  }

  private static String createSinglePayloadFilter(Collection<String> values) {
    if (values.isEmpty()) {
      return "";
    }
    if (values.size() == 1) {
      return "payload->>? = ? ";
    } else {
      return String.format(
          "payload->>? in (%s)", createJdbTemplateIndicators(values.size()));
    }
  }

  private static AlertDto getAlertDto(ResultSet rs) throws SQLException {
    return AlertDto
        .builder()
        .id(rs.getLong("id"))
        .name(rs.getString("name"))
        .discriminator(rs.getString("discriminator"))
        .recommendationDate(rs.getTimestamp("recommendation_date"))
        .createdAt(rs.getTimestamp(AlertColumnName.CREATED_AT.getName()))
        .payload(rs.getString("payload"))
        .build();
  }
}
