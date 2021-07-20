package com.silenteight.warehouse.indexer.query.grouping;

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

public class SqlBuilder {

  static final String KEY_COUNT = "count(*)";
  private static final DateTimeFormatter ES_DATETIME_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  static {
    preventDisplayingJooqLogo();
  }

  private static void preventDisplayingJooqLogo() {
    System.getProperties().setProperty("org.jooq.no-logo", "true");
  }

  public String groupByBetweenDates(
      List<String> indexes, List<String> groupByFields,
      String dateField, OffsetDateTime from, OffsetDateTime to) {

    List<SelectFieldOrAsterisk> selectColumns =
        concat(asFields(groupByFields), of(count()))
            .collect(toList());

    List<Table<Record>> tables = asTables(indexes)
        .collect(toList());

    List<Field<Object>> groupByColumns = asFields(groupByFields)
        .collect(toList());

    Condition dateTimeFrom = field(dateField).greaterOrEqual(asTimestamp(from));
    Condition dateTimeTo = field(dateField).lessThan(asTimestamp(to));

    return select(selectColumns)
        .from(tables)
        .where(dateTimeFrom, dateTimeTo)
        .groupBy(groupByColumns)
        .getSQL();
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
}
