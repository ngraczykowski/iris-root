package com.silenteight.warehouse.report.rbs.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.commons.CSVUtils;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.GroupingColumnProperties;
import com.silenteight.warehouse.report.reporting.GroupingValues;
import com.silenteight.warehouse.report.reporting.RbsReportDefinition;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.validation.Valid;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
public class RbsReportGenerationService {

  private static final String DELIMITER = ",";
  private static final String EMPTY_STRING = "";
  @NonNull
  private final GroupingQueryService groupingQueryService;

  public CsvReportContentDto generateReport(
      @NonNull OffsetDateTime from,
      @NonNull OffsetDateTime to,
      @NonNull List<String> indexes,
      @NonNull @Valid RbsReportDefinition properties) {

    FetchGroupedDataResponse rawData = fetchRawData(from, to, indexes, properties);
    return CsvReportContentDto.of(getLabelsRow(properties), transpose(rawData, properties));
  }

  private String getLabelsRow(RbsReportDefinition properties) {
    return CSVUtils.getCSVRecordWithDefaultDelimiter(
        properties.getListOfLabels().toArray(String[]::new));
  }

  private FetchGroupedDataResponse fetchRawData(
      OffsetDateTime from,
      OffsetDateTime to,
      List<String> indexes,
      RbsReportDefinition properties) {

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest
        .builder()
        .from(from)
        .to(to)
        .dateField(properties.getDateFieldName())
        .fields(properties.getListOfFields())
        .queryFilters(properties.getQueryFilters())
        .indexes(indexes)
        .build();

    return groupingQueryService.generate(request);
  }

  private List<String> transpose(FetchGroupedDataResponse rawData, RbsReportDefinition properties) {
    Map<String, List<Row>> rowsToTranspose = rawData
        .getRows()
        .stream()
        .collect(groupingBy(row -> generateStaticFields(row, properties)));

    return rowsToTranspose
        .keySet()
        .stream()
        .map(key -> getLine(rowsToTranspose.get(key), properties))
        .collect(toList());
  }

  private static String generateStaticFields(Row row, RbsReportDefinition properties) {
    return properties
        .getListOfStaticFields()
        .stream()
        .map(fieldName -> row.getValueOrDefault(fieldName, EMPTY_STRING))
        .collect(joining(DELIMITER));
  }

  private String getLine(List<Row> rowsToTranspose, RbsReportDefinition properties) {
    Stream<String> transposedCells = properties
        .getGroupingColumns()
        .stream()
        .flatMap(column -> getValues(column, rowsToTranspose));

    Row rowWithGroupedData = rowsToTranspose.stream().findAny().orElseThrow();
    Stream<String> staticCells = properties
        .getListOfStaticFields()
        .stream()
        .map(fieldName -> rowWithGroupedData.getValueOrDefault(fieldName, EMPTY_STRING));

    long matchCount = rowsToTranspose.stream().mapToLong(Row::getCount).sum();
    Stream<String> staticCellsWithCount =
        concat(staticCells, Stream.of(String.valueOf(matchCount)));

    Stream<String> rowCells = concat(staticCellsWithCount, transposedCells);
    return CSVUtils.getCSVRecordWithDefaultDelimiter(rowCells.toArray(String[]::new));
  }

  private static Stream<String> getValues(GroupingColumnProperties column, List<Row> rows) {
    Map<String, Long> values = rows
        .stream()
        .collect(toMap(
            row -> row.getValueOrDefault(column.getName(), EMPTY_STRING).toLowerCase(),
            Row::getCount,
            Long::sum));

    return column.getGroupingValues()
        .stream()
        .map(GroupingValues::getValue)
        .map(String::toLowerCase)
        .map(groupingValue -> values.getOrDefault(groupingValue, 0L))
        .map(String::valueOf);
  }
}
