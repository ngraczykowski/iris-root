package com.silenteight.warehouse.indexer.query.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.OTHER;

@Slf4j
@RequiredArgsConstructor
public class SqlBuilder {

  public static final String KEY_COUNT = "count(*)";
  private static final DateTimeFormatter ES_DATETIME_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
      List<String> indexes,
      List<String> groupByFields,
      List<MultiValueCondition> where,
      SingleValueCondition from,
      SingleValueCondition to) {

    List<SelectFieldOrAsterisk> selectColumns =
        concat(asFields(groupByFields), of(count()))
            .collect(toList());
    List<Table<Record>> tables = asTables(indexes)
        .collect(toList());
    List<Condition> conditions = asConditions(from, to, where);
    SelectConditionStep<Record> sqlQuery = select(selectColumns)
        .from(tables)
        .where(conditions);

    List<Field<Object>> groupByColumns = asFields(groupByFields)
        .collect(toList());
    if (!groupByColumns.isEmpty())
      sqlQuery.groupBy(groupByColumns);

    String sql = sqlQuery.getSQL();
    log.trace("SqlBuilder: {}", sql);

    return sql;
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
        .map(SqlBuilder::surroundWithApostrophes)
        .map(DSL::table);
  }

  private static Field<Object> asTimestamp(OffsetDateTime datetime) {
    String formattedDateTime = datetime.atZoneSameInstant(UTC).format(ES_DATETIME_FORMAT);
    return function("timestamp", OTHER, inline(formattedDateTime));
  }

  private static List<Condition> asConditions(
      SingleValueCondition from, SingleValueCondition to, List<MultiValueCondition> conditions) {

    Condition fromCondition = field(from.getField()).greaterOrEqual(asTimestamp(from.getValue()));
    Condition toCondition = field(to.getField()).lessThan(asTimestamp(to.getValue()));

    Stream<Condition> whereConditions = conditions.stream()
        .map(SqlBuilder::anyValueMatch);

    return of(of(fromCondition), of(toCondition), whereConditions)
        .flatMap(identity())
        .collect(toList());
  }

  private static Condition anyValueMatch(MultiValueCondition condition) {
    return condition.getValues().stream()
        .map(value -> field(condition.getField()).eq(inline(value)))
        .reduce(noCondition(), Condition::or);
  }
}
