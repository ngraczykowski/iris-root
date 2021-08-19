package com.silenteight.warehouse.report.metrics.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.commons.CSVUtils.getCSVRecordWithDefaultDelimiter;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

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
    return CsvReportContentDto.of(getLabelsRow(), transpose(rawData, properties));
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
        .dateField(properties.getDateFieldName())
        .fields(properties.getFields())
        .indexes(indexes)
        .build();

    return groupingQueryService.generate(request);
  }

  private static String getLabelsRow() {
    return getCSVRecordWithDefaultDelimiter(
        "Country", "Risk Type", "Efficiency");
  }

  private static List<String> transpose(
      FetchGroupedDataResponse rawData, PropertiesDefinition properties) {

    Map<String, List<Row>> rowsToTranspose = rawData
        .getRows()
        .stream()
        .collect(groupingBy(v -> generateStaticFields(v, properties.getStaticFields())));

    return rowsToTranspose
        .values()
        .stream()
        .map(row -> getLine(row, properties))
        .sorted()
        .collect(toList());
  }

  private static String getLine(List<Row> rows, PropertiesDefinition properties) {
    String country = getStaticValue(rows, properties.getCountry().getName());
    String riskType = getStaticValue(rows, properties.getRiskType().getName());

    long allAlertsInGroupCount = rows.stream().mapToLong(Row::getCount).sum();
    long meaningfulDecisionCount = rows
        .stream()
        .filter(v -> isMeaningfulDecision(v, properties.getRecommendationField()))
        .mapToLong(Row::getCount)
        .sum();

    double efficiency = (double) meaningfulDecisionCount / allAlertsInGroupCount;

    return getCSVRecordWithDefaultDelimiter(
        country, riskType, DECIMAL_FORMAT.format(efficiency));
  }

  private static boolean isMeaningfulDecision(
      Row row, GroupingColumnProperties recommendationField) {
    return recommendationField
        .getDecisionValues()
        .contains(row.getValueOrDefault(
            recommendationField.getName(), EMPTY_STRING));
  }

  private static String getStaticValue(List<Row> rows, String field) {
    return rows
        .stream()
        .findFirst()
        .map(row -> row.getValueOrDefault(field, EMPTY_STRING))
        .orElseThrow();
  }

  private static String generateStaticFields(Row row, List<String> staticFields) {
    return staticFields
        .stream()
        .map(fieldName -> row.getValueOrDefault(fieldName, EMPTY_STRING))
        .collect(joining(DELIMITER));
  }
}
