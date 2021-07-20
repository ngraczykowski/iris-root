package com.silenteight.warehouse.report.billing.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.commons.CSVUtils;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
public class BillingReportGenerationService {

  private static final String DELIMITER = ",";
  @NonNull
  private final GroupingQueryService groupingQueryService;
  @NonNull
  private final IndexesQuery indexerQuery;
  @NonNull
  private final BillingReportProperties properties;

  public CsvReportContentDto generateReport(
      OffsetDateTime from, OffsetDateTime to, String analysisName) {

    FetchGroupedDataResponse rawData = fetchRawData(from, to, analysisName);
    List<String> reportData = transpose(rawData);
    String checksum = getChecksumLine(reportData);

    return new CsvReportContentDto(getLabelsRow(), reportData, checksum);
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

  private String getLabelsRow() {
    return CSVUtils.getCSVRecordWithDefaultDelimiter(
        properties.getListOfLabels().toArray(String[]::new));
  }

  private List<String> transpose(FetchGroupedDataResponse rawData) {
    Map<String, List<Row>> rowsToTranspose = rawData
        .getRows()
        .stream()
        .collect(groupingBy(this::generateStaticFields));

    return rowsToTranspose
        .keySet()
        .stream()
        .sorted()
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
    Stream<String> transposedCells = getValues(properties.getTransposeColumn(), rowsToTranspose);

    Row rowWithGroupedData = rowsToTranspose.stream().findAny().orElseThrow();
    Stream<String> staticCells = properties
        .getListOfStaticFields().stream().map(rowWithGroupedData::getValue);

    Stream<String> rowCells = concat(staticCells, transposedCells);
    return CSVUtils.getCSVRecordWithDefaultDelimiter(rowCells.toArray(String[]::new));
  }

  private Stream<String> getValues(TransposeColumnProperties column, List<Row> rows) {
    List<String> result = new ArrayList<>();
    Map<String, Long> values = rows.stream().collect(
        toMap(row -> row.getValue(column.getName()).toLowerCase(), Row::getCount, Long::sum));

    column.getGroupingValues()
          .stream()
          .map(String::toLowerCase)
          .map(groupingValue -> values.getOrDefault(groupingValue, 0L))
          .map(String::valueOf)
          .forEach(result::add);

    result.add(getAllSignificantValuesSum(values,
                                          getSignificantValues()));
    result.add(getAllValuesSum(values));

    return result.stream();
  }

  @NotNull
  private List<String> getSignificantValues() {
    return properties
        .getTransposeColumn()
        .getSignificantValues()
        .stream()
        .map(String::toLowerCase)
        .collect(toList());
  }

  private static String getAllSignificantValuesSum(
      Map<String, Long> values, List<String> significantValues) {
    return valueOf(values
        .keySet()
        .stream()
        .filter(significantValues::contains)
        .mapToLong(values::get)
        .sum());
  }

  private static String getAllValuesSum(Map<String, Long> values) {
    return valueOf(values.values().stream().mapToLong(l -> l).sum());
  }

  private static String getChecksumLine(List<String> reportData) {
    return CSVUtils.getCSVRecordWithDefaultDelimiter(
        "checksum", ChecksumGenerator.generateChecksum(reportData));
  }
}
