package com.silenteight.warehouse.report.rbs.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.commons.CSVUtils;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
public class RbsReportGenerationService {

  private static final String DELIMITER = ",";
  @NonNull
  private final GroupingQueryService groupingQueryService;
  @NonNull
  private final IndexesQuery indexerQuery;
  @NonNull
  private final RbsReportProperties properties;

  public CsvReportContentDto generateReport(
      OffsetDateTime from, OffsetDateTime to, String analysisName) {
    FetchGroupedDataResponse rawData = fetchRawData(from, to, analysisName);
    return CsvReportContentDto.of(getLabelsRow(), transpose(rawData));
  }

  private String getLabelsRow() {
    return CSVUtils.getCSVRecordWithDefaultDelimiter(
        properties.getListOfLabels().toArray(String[]::new));
  }

  private FetchGroupedDataResponse fetchRawData(
      OffsetDateTime from, OffsetDateTime to, String analysisName) {

    List<String> indexes = indexerQuery.getIndexesForAnalysis(analysisName);
    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest
        .builder()
        .from(from)
        .to(to)
        .dateField(properties.getDateFieldName())
        .fields(properties.getListOfFields())
        .indexes(indexes)
        .build();
    return groupingQueryService.generate(request);
  }

  private List<String> transpose(FetchGroupedDataResponse rawData) {
    Map<String, List<Row>> rowsToTranspose = rawData
        .getRows()
        .stream()
        .collect(groupingBy(this::generateStaticFields));

    return rowsToTranspose
        .keySet()
        .stream()
        .map(key -> getLine(rowsToTranspose.get(key)))
        .collect(toList());
  }

  private String generateStaticFields(Row row) {
    return properties.getListOfStaticFields()
        .stream()
        .map(row::getValue)
        .collect(joining(DELIMITER));
  }

  private String getLine(List<Row> rowsToTranspose) {
    Stream<String> transposedCells = properties
        .getGroupingColumns()
        .stream()
        .flatMap(column -> getValues(column, rowsToTranspose));

    Row rowWithGroupedData = rowsToTranspose.stream().findAny().orElseThrow();
    Stream<String> staticCells = properties
        .getListOfStaticFields().stream().map(rowWithGroupedData::getValue);

    Stream<String> rowCells = concat(staticCells, transposedCells);
    return CSVUtils.getCSVRecordWithDefaultDelimiter(rowCells.toArray(String[]::new));
  }

  private Stream<String> getValues(GroupingColumnProperties column, List<Row> rows) {
    List<String> result = new ArrayList<>();
    Map<String, Long> values = rows.stream().collect(
        toMap(row -> row.getValue(column.getName()).toLowerCase(), Row::getCount, Long::sum));

    if (column.isAddCounter())
      result.add(getAllNonNullValueSum(values));

    column.getGroupingValues()
        .stream()
        .map(String::toLowerCase)
        .map(groupingValue -> values.getOrDefault(groupingValue, 0L))
        .map(String::valueOf)
        .forEach(result::add);

    return result.stream();
  }

  private static String getAllNonNullValueSum(Map<String, Long> values) {
    long result = values.entrySet()
        .stream()
        .filter(entry -> StringUtils.isNotBlank(entry.getKey()))
        .mapToLong(Entry::getValue)
        .sum();
    return valueOf(result);
  }
}
