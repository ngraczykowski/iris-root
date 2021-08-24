package com.silenteight.warehouse.report.metrics.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.commons.CSVUtils.getCSVRecordWithDefaultDelimiter;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RequiredArgsConstructor
public class MetricsReportGenerationService {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000");
  private static final String EMPTY_STRING = "";
  private static final String DELIMITER = ",";

  @NonNull
  private final GroupingQueryService groupingQueryService;

  public CsvReportContentDto generateReport(
      OffsetDateTime from,
      OffsetDateTime to,
      List<String> indexes,
      @Valid PropertiesDefinition properties) {

    FetchGroupedDataResponse rawData = fetchRawData(from, to, indexes, properties);
    return CsvReportContentDto.of(getLabelsRow(properties), transpose(rawData, properties));
  }

  private FetchGroupedDataResponse fetchRawData(
      OffsetDateTime from,
      OffsetDateTime to,
      List<String> indexes,
      PropertiesDefinition properties) {

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest
        .builder()
        .from(from)
        .to(to)
        .dateField(properties.getDateField().getName())
        .fields(properties.getFields())
        .indexes(indexes)
        .build();

    return groupingQueryService.generate(request);
  }

  private static String getLabelsRow(@Valid PropertiesDefinition properties) {
    return getCSVRecordWithDefaultDelimiter(
        properties.getDateField().getLabel(),
        properties.getCountry().getLabel(),
        properties.getRiskType().getLabel(),
        properties.getHitType().getLabel(),
        "Efficiency",
        "PTP Effectiveness",
        "FP Effectiveness");
  }

  private static List<String> transpose(
      FetchGroupedDataResponse rawData, PropertiesDefinition properties) {

    Map<String, List<Row>> rowsToTranspose = rawData
        .getRows()
        .stream()
        .collect(groupingBy(row -> getGroupingValues(row, properties.getGroupingColumns())));

    return rowsToTranspose
        .values()
        .stream()
        .map(row -> getLine(row, properties))
        .sorted()
        .collect(toList());
  }

  private static String getLine(List<Row> rows, PropertiesDefinition properties) {
    ColumnProperties recommendationField = properties.getRecommendationField();
    ColumnProperties analystDecisionField = properties.getAnalystDecisionField();
    ColumnProperties qaDecisionField = properties.getQaDecisionField();

    double efficiency = calculateEfficiency(rows, recommendationField);
    List<Row> potentialTruePositiveRows = getPotentialTruePositiveRows(rows, recommendationField);
    double potentialTruePositiveEffectiveness =
        calculateEffectiveness(potentialTruePositiveRows, analystDecisionField);

    List<Row> falsePositiveRows = getFalsePositiveRows(rows, recommendationField);
    double falsePositiveEffectiveness =
        calculateEffectiveness(falsePositiveRows, qaDecisionField);

    String date = getStaticValue(rows, properties.getDateField());
    String country = getStaticValue(rows, properties.getCountry());
    String riskType = getStaticValue(rows, properties.getRiskType());
    String hitType = getStaticValue(rows, properties.getHitType());
    return getCSVRecordWithDefaultDelimiter(
        date,
        country,
        riskType,
        hitType,
        DECIMAL_FORMAT.format(efficiency),
        DECIMAL_FORMAT.format(potentialTruePositiveEffectiveness),
        DECIMAL_FORMAT.format(falsePositiveEffectiveness));
  }

  private static List<Row> getPotentialTruePositiveRows(List<Row> rows, ColumnProperties column) {
    return rows
        .stream()
        .filter(row -> isPositiveValue(row, column))
        .collect(toList());
  }

  private static List<Row> getFalsePositiveRows(List<Row> rows, ColumnProperties column) {
    return rows
        .stream()
        .filter(row -> isNegativeValue(row, column))
        .collect(toList());
  }

  private static double calculateEffectiveness(List<Row> rows, ColumnProperties column) {
    long positiveCount = rows
        .stream()
        .filter(row -> isPositiveValue(row, column))
        .mapToLong(Row::getCount)
        .sum();

    long meaningfulDecisionCount = countMeaningfulDecision(rows, column);
    return divide(positiveCount, meaningfulDecisionCount);
  }

  private static double calculateEfficiency(List<Row> rows, ColumnProperties column) {
    long meaningfulDecisionCount = countMeaningfulDecision(rows, column);
    long allAlertsInGroupCount = rows.stream().mapToLong(Row::getCount).sum();

    return divide(meaningfulDecisionCount, allAlertsInGroupCount);
  }

  private static long countMeaningfulDecision(List<Row> rows, ColumnProperties column) {
    return rows
        .stream()
        .filter(row -> isMeaningfulDecision(row, column))
        .mapToLong(Row::getCount)
        .sum();
  }

  private static boolean isPositiveValue(Row row, ColumnProperties column) {
    return column
        .getPositiveValue()
        .equals(row.getValueOrDefault(column.getName(), EMPTY_STRING));
  }

  private static boolean isNegativeValue(Row row, ColumnProperties column) {
    return column
        .getNegativeValue()
        .equals(row.getValueOrDefault(column.getName(), EMPTY_STRING));
  }

  private static boolean isMeaningfulDecision(Row row, ColumnProperties column) {
    return column
        .getDecisionValues()
        .contains(row.getValueOrDefault(column.getName(), EMPTY_STRING));
  }

  private static String getStaticValue(List<Row> rows, GroupingColumnProperties column) {
    return rows
        .stream()
        .findFirst()
        .map(row -> getFormattedValueOrDefault(row, column))
        .orElseThrow();
  }

  private static String getGroupingValues(Row row, List<GroupingColumnProperties> columns) {
    return columns
        .stream()
        .map(column -> getFormattedValueOrDefault(row, column))
        .collect(joining(DELIMITER));
  }

  private static String getFormattedValueOrDefault(Row row, GroupingColumnProperties column) {
    String value = row.getValueOrDefault(column.getName(), EMPTY_STRING);
    if (column.isDateColumn() && isNotBlank(value))
      value = formatDate(value, column);

    return value;
  }

  private static String formatDate(String value, GroupingColumnProperties column) {
    DateTimeFormatter sourceFormatter = ofPattern(column.getSourcePattern());
    LocalDate date = parse(value, sourceFormatter);
    DateTimeFormatter targetFormatter = ofPattern(column.getTargetPattern());
    return date.format(targetFormatter);
  }

  private static double divide(long dividend, long divisor) {
    return divisor == 0 ? 0 : (double) dividend / divisor;
  }
}
