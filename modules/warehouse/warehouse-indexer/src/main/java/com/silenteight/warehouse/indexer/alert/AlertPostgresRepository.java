package com.silenteight.warehouse.indexer.alert;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.indexer.alert.dto.AlertGroupingDto;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.StringUtils;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.time.Timestamps.toSqlTimestamp;

@RequiredArgsConstructor
@Slf4j
public final class AlertPostgresRepository implements AlertRepository {

  @Language("PostgreSQL")
  private static final String FETCH =
      "SELECT * FROM warehouse_alert WHERE name IS NOT NULL AND %s BETWEEN ? AND ?%s%s ORDER BY"
          + " RANDOM() LIMIT ?";
  @Language("PostgreSQL")
  private static final String FETCH_ALERT_BETWEEN_RECOMMENDATION_DATE =
      "SELECT * FROM warehouse_alert WHERE name IS NOT NULL AND recommendation_date"
          + " BETWEEN ? AND ?";
  @Language("PostgreSQL")
  private static final String FETCH_ALL_WITH_RECOMMENDATION_DATE_ORDERED =
      "SELECT recommendation_date FROM warehouse_alert WHERE name IS NOT NULL AND"
          + " recommendation_date IS NOT NULL ORDER BY recommendation_date LIMIT 1";
  @Language("PostgreSQL")
  private static final String GROUP_BY =
      "SELECT %s FROM warehouse_alert WHERE %s BETWEEN ? AND ?%s";
  @Language("PostgreSQL")
  private static final String CHECK_IF_KEY_EXISTS =
      "SELECT EXISTS(SELECT 1 FROM warehouse_alert WHERE (payload->?) IS NOT NULL)";
  private static final String ALERT_NAME_FILTER = "AND name IN (%s)";
  private static final String JDBC_TEMPLATE_PARAMETER_INDICATOR = "?,";

  @Valid
  @NonNull
  private JdbcTemplate jdbcTemplate;

  @Valid
  @NonNull
  private AlertDtoRowMapper mapper;

  private static AlertGroupingDto getAlertGroupingDto(
      Entry<Map<String, String>, Long> groupedFieldsEntry) {
    return AlertGroupingDto
        .builder()
        .count(groupedFieldsEntry.getValue().intValue())
        .groupedNameValueFields(groupedFieldsEntry.getKey()).build();
  }

  private static List<Object> createJdbcParameters(
      Timestamp timeFrom, Timestamp timeTo, ListMultimap<String, List<String>> filters,
      List<String> alertNames, Optional<Integer> limit) {
    ImmutableList.Builder<Object> paramsBuilder = new ImmutableList.Builder<>();
    // Temporary we have to switch string again to timestamp to support ES and postgres at the
    // same time when ES is deprecated we can use timestamp instead of string as method parameter
    paramsBuilder.add(timeFrom);
    paramsBuilder.add(timeTo);
    paramsBuilder.addAll(alertNames);
    filters.entries().forEach((entry -> {
      paramsBuilder.add(entry.getKey());
      paramsBuilder.addAll(entry.getValue());
    }));
    limit.ifPresent(paramsBuilder::add);
    return paramsBuilder.build();
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

  private static String getSelectColumns(List<String> fields) {
    return fields.stream()
        .map(field -> String.format(
            "payload ->> '%s' AS \"%s\"",
            field,
            fields.indexOf(field)))
        .collect(Collectors.joining(", "));
  }

  @Override
  public List<AlertDto> fetchRandomAlerts(
      AlertColumnName alertColumnName, String timeFrom, String timeTo, int limit,
      ListMultimap<String, List<String>> filters, List<String> alertNames) {

    String sql =
        String.format(
            FETCH, alertColumnName.getName(), getAlertNameFilter(alertNames),
            getPayloadFilters(filters));

    List<Object> jdbcParameters =
        createJdbcParameters(
            toSqlTimestamp(timeFrom), toSqlTimestamp(timeTo), filters, alertNames,
            Optional.of(limit));

    log.debug("Querying jdbctemplate with sql {} with parameters {}", sql, jdbcParameters);
    return jdbcTemplate.query(sql, mapper, jdbcParameters.toArray(new Object[0]));
  }

  @Override
  public List<AlertGroupingDto> fetchGroupedAlerts(
      AlertColumnName alertColumnName, OffsetDateTime timeFrom, OffsetDateTime timeTo,
      ListMultimap<String, List<String>> filters, List<String> groupByFields) {
    verifyGroupByFields(groupByFields);
    String payloadFilters = getPayloadFilters(filters);

    String sql = String.format(
        GROUP_BY,
        getSelectColumns(groupByFields),
        alertColumnName.getName(),
        payloadFilters);

    List<Object> jdbcParameters =
        createJdbcParameters(toSqlTimestamp(timeFrom), toSqlTimestamp(timeTo), filters,
            List.of(), Optional.empty());

    log.debug("Querying jdbctemplate with sql {} with parameters {}", sql, jdbcParameters);
    return jdbcTemplate
        .query(sql, (rs, rowNum) -> getFieldsValueMap(rs, groupByFields),
            jdbcParameters.toArray(new Object[0]))
        .stream()
        // Grouping is not done on the database side as it is hard to use jdbctemplate with dynamic
        // properties and jsonb to (apostrophe is required which is not allowed in jdbctemplate)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet()
        .stream()
        .map(AlertPostgresRepository::getAlertGroupingDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<AlertDto> fetchAlerts(OffsetDateTime timeFrom, OffsetDateTime timeTo) {
    return jdbcTemplate.query(
        FETCH_ALERT_BETWEEN_RECOMMENDATION_DATE, mapper, toSqlTimestamp(timeFrom),
        toSqlTimestamp(timeTo));
  }

  @Override
  public LocalDate getEarliestAlertLocaDate() {
    return jdbcTemplate.queryForObject(FETCH_ALL_WITH_RECOMMENDATION_DATE_ORDERED, LocalDate.class);
  }

  private Map<String, String> getFieldsValueMap(ResultSet rs, List<String> fields) {
    try {
      ImmutableMap.Builder<String, String> mapBuilder = ImmutableMap.builder();
      for (String field : fields) {
        Optional.ofNullable(rs.getString(String.valueOf(fields.indexOf(field))))
            .ifPresent(value -> mapBuilder.put(field, value));
      }
      return mapBuilder.build();
    } catch (SQLException e) {
      throw new IllegalStateException("Error while precessing alert table result set", e);
    }
  }

  private void verifyGroupByFields(List<String> groupByFields) {
    for (String field : groupByFields) {
      Boolean exists = jdbcTemplate.queryForObject(CHECK_IF_KEY_EXISTS, Boolean.TYPE, field);
      if (Boolean.FALSE.equals(exists)) {
        throw new GroupingFieldNotFoundException(field);
      }
    }
  }
}
