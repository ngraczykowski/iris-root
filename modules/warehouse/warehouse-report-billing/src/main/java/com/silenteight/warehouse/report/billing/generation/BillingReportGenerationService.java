package com.silenteight.warehouse.report.billing.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.validation.Valid;

import static com.silenteight.commons.CSVUtils.getCSVRecordWithDefaultDelimiter;
import static com.silenteight.warehouse.report.billing.generation.ChecksumGenerator.generateChecksum;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang3.StringUtils.leftPad;

@RequiredArgsConstructor
public class BillingReportGenerationService {

  private static final String DELIMITER = "-";
  private static final String EMPTY_STRING = "";
  private static final String CHECKSUM = "checksum";

  @NonNull
  private final GroupingQueryService groupingQueryService;
  @Valid
  @NonNull
  private final BillingReportProperties properties;

  public CsvReportContentDto generateReport(
      @NonNull OffsetDateTime from,
      @NonNull OffsetDateTime to,
      @NonNull List<String> indexes) {

    FetchGroupedDataResponse rawData = fetchRawData(from, to, indexes);
    List<String> reportData = transpose(rawData);
    String checksum = getChecksumLine(reportData);
    return new CsvReportContentDto(getLabelsRow(), reportData, checksum);
  }

  private FetchGroupedDataResponse fetchRawData(
      OffsetDateTime from,
      OffsetDateTime to,
      List<String> indexes) {

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

  private String getLabelsRow() {
    return getCSVRecordWithDefaultDelimiter(properties.getLabels().toArray(String[]::new));
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
        .sorted()
        .collect(toList());
  }

  private String generateStaticFields(Row row) {
    return properties.getDateColumnsLabel()
        .stream()
        .map(fieldName -> row.getValueOrDefault(fieldName, EMPTY_STRING))
        .collect(joining(DELIMITER));
  }

  private String getLine(List<Row> rowsToTranspose) {
    Stream<String> transposedCells = getValues(properties.getTransposeColumn(), rowsToTranspose);
    Row rowWithGroupedData = rowsToTranspose.stream().findAny().orElseThrow();

    String staticCells = properties
        .getDateColumnsLabel()
        .stream()
        .map(fieldName -> rowWithGroupedData.getValueOrDefault(fieldName, EMPTY_STRING))
        .map(BillingReportGenerationService::convertToTwoDigitValue)
        .collect(joining(DELIMITER));

    Stream<String> staticFieldsStream = Stream.<String>builder()
        .add(staticCells)
        .build();

    Stream<String> rowCells = concat(staticFieldsStream, transposedCells);

    return getCSVRecordWithDefaultDelimiter(rowCells.toArray(String[]::new));
  }

  private static String convertToTwoDigitValue(String field) {
    return leftPad(field, 2, "0");
  }

  private Stream<String> getValues(TransposeColumnProperties column, List<Row> rows) {
    Map<String, Long> values = rows
        .stream()
        .collect(toMap(
            row -> row.getValueOrDefault(column.getName(), EMPTY_STRING).toLowerCase(),
            Row::getCount,
            Long::sum));

    List<String> result = column.getGroupingValues()
        .stream()
        .map(ColumnProperties::getName)
        .map(groupingValue -> values.getOrDefault(groupingValue.toLowerCase(), 0L))
        .map(String::valueOf)
        .collect(toList());

    result.add(getAllSignificantValuesSum(
        values,
        getSignificantValues()));

    result.add(getAllValuesSum(values.values()));
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

  private static String getAllValuesSum(Collection<Long> values) {
    return valueOf(values.stream().mapToLong(Long::longValue).sum());
  }

  private static String getChecksumLine(List<String> reportData) {
    return getCSVRecordWithDefaultDelimiter(
        CHECKSUM, generateChecksum(reportData));
  }
}
