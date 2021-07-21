package com.silenteight.warehouse.report.sm.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.commons.CSVUtils;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.sm.generation.dto.CsvReportContentDto;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SimulationMetricsReportGenerationService {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000");

  private static final String EMPTY_STRING = "";
  private static final String DELIMITER = ",";
  private static final DefaultTimeSource INSTANCE = DefaultTimeSource.INSTANCE;
  private static final TimeConverter TIME_CONVERTER = DefaultTimeSource.TIME_CONVERTER;
  private static final OffsetDateTime EPOCH = TIME_CONVERTER.toOffset(Instant.EPOCH);
  private static final OffsetDateTime NOW = TIME_CONVERTER.toOffset(INSTANCE.now());


  @NonNull
  private final GroupingQueryService groupingQueryService;
  @NonNull
  private final IndexesQuery indexerQuery;
  @NonNull
  private final SimulationMetricsReportProperties properties;

  public CsvReportContentDto generateReport(String analysisName) {
    FetchGroupedDataResponse rawData = fetchRawData(analysisName);
    return CsvReportContentDto.of(getLabelsRow(), transpose(rawData));
  }

  private FetchGroupedDataResponse fetchRawData(String analysisName) {
    List<String> indexes = indexerQuery.getIndexesForAnalysis(analysisName);
    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest
        .builder()
        .from(EPOCH)
        .to(NOW)
        .dateField(properties.getDateFieldName())
        .fields(properties.getFields())
        .indexes(indexes)
        .build();
    return groupingQueryService.generate(request);
  }

  private String getLabelsRow() {
    return CSVUtils.getCSVRecordWithDefaultDelimiter(
        "Country", "Risk Type", "Efficiency");
  }

  private List<String> transpose(FetchGroupedDataResponse rawData) {
    Map<String, List<Row>> rowsToTranspose = rawData
        .getRows()
        .stream()
        .collect(groupingBy(this::generateStaticFields));

    return rowsToTranspose
        .values()
        .stream()
        .map(this::getLine)
        .sorted()
        .collect(toList());
  }

  private String getLine(List<Row> rows) {
    String country = getStaticValue(rows, properties.getCountry().getName());
    String riskType = getStaticValue(rows, properties.getRiskType().getName());

    long allAlertsInGroupCount = rows.stream().mapToLong(Row::getCount).sum();
    long meaningfulDecisionCount = rows
        .stream()
        .filter(this::isMeaningfulDecision)
        .mapToLong(Row::getCount)
        .sum();

    double efficiency = (double) meaningfulDecisionCount / allAlertsInGroupCount;

    return CSVUtils.getCSVRecordWithDefaultDelimiter(
        country, riskType, DECIMAL_FORMAT.format(efficiency));
  }

  private boolean isMeaningfulDecision(Row row) {
    return properties
        .getRecommendationField()
        .getDecisionValues()
        .contains(row.getValueOrDefault(
            properties.getRecommendationField().getName(), EMPTY_STRING));
  }

  private String getStaticValue(List<Row> rows, String field) {
    return rows
        .stream()
        .findFirst()
        .map(row -> row.getValueOrDefault(field, EMPTY_STRING))
        .orElseThrow();
  }

  private String generateStaticFields(Row row) {
    return properties
        .getStaticFields()
        .stream()
        .map(fieldName -> row.getValueOrDefault(fieldName, EMPTY_STRING))
        .collect(joining(DELIMITER));
  }
}
