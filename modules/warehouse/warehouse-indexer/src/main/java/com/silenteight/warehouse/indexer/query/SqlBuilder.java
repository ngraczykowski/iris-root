package com.silenteight.warehouse.indexer.query;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.OTHER;

@RequiredArgsConstructor
public class SqlBuilder {

  public static final String KEY_COUNT = "count(*)";
  private static final DateTimeFormatter ES_DATETIME_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
  private final String alertStatusField;
  private final String alertStatusCompleted;

  static {
    preventDisplayingJooqLogo();
  }

  private static void preventDisplayingJooqLogo() {
    System.getProperties().setProperty("org.jooq.no-logo", "true");
  }

  public String describeTables(String index) {
    return "DESCRIBE TABLES LIKE " + index;
  }

  public String groupByBetweenDates(
      List<String> indexes, List<String> groupByFields,
      String dateField, OffsetDateTime from, OffsetDateTime to, boolean onlySolvedAlerts) {

    List<SelectFieldOrAsterisk> selectColumns =
        concat(asFields(groupByFields), of(count()))
            .collect(toList());

    List<Table<Record>> tables = asTables(indexes)
        .collect(toList());

    List<Field<Object>> groupByColumns = asFields(groupByFields)
        .collect(toList());

    SelectConditionStep<Record> sqlQuery = select(selectColumns)
        .from(tables)
        .where(getConditions(from, to, dateField, onlySolvedAlerts));

    if (!groupByColumns.isEmpty())
      sqlQuery.groupBy(groupByColumns);

    return sqlQuery.getSQL();
  }

  private static Stream<Field<Object>> asFields(List<String> fields) {
    return fields.stream()
        .map(SqlBuilder::surroundWithApostrophes)
        .map(DSL::field);
  }

  @NotNull
  private static String surroundWithApostrophes(String field) {
    return "`" + field + "`";
  }

  private static Stream<Table<Record>> asTables(List<String> indexes) {
    return indexes.stream()
        .map(DSL::table);
  }

  private static Field<Object> asTimestamp(OffsetDateTime datetime) {
    String formattedDateTime = datetime.atZoneSameInstant(UTC).format(ES_DATETIME_FORMAT);
    return function("timestamp", OTHER, inline(formattedDateTime));
  }

  private List<Condition> getConditions(
      OffsetDateTime from, OffsetDateTime to, String dateField, boolean onlySolvedAlerts) {

    Condition dateTimeFrom = field(dateField).greaterOrEqual(asTimestamp(from));
    Condition dateTimeTo = field(dateField).lessThan(asTimestamp(to));

    if (onlySolvedAlerts) {
      Condition completedAlerts = field(alertStatusField)
          .eq(inline(alertStatusCompleted));

      return List.of(dateTimeFrom, dateTimeTo, completedAlerts);
    } else {
      return List.of(dateTimeFrom, dateTimeTo);
    }
  }
}
