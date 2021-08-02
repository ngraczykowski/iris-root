package com.silenteight.warehouse.indexer.query.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.QueryDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto.SchemaEntry;
import com.silenteight.warehouse.indexer.query.SqlBuilder;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.index.QueryIndexService;

import java.util.*;

import static com.silenteight.warehouse.indexer.query.SqlBuilder.KEY_COUNT;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class GroupingQueryService {

  static final String EMPTY_VALUE_PLACEHOLDER = "";

  @NonNull
  private final SqlBuilder sqlBuilder;
  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;
  @NonNull
  private final QueryIndexService queryIndexService;

  public FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest) {

    List<String> existingFields = filterOutNonExistingFields(
        fetchGroupedDataRequest.getIndexes(), fetchGroupedDataRequest.getFields());

    String query = sqlBuilder.groupByBetweenDates(
        fetchGroupedDataRequest.getIndexes(),
        existingFields,
        fetchGroupedDataRequest.getDateField(),
        fetchGroupedDataRequest.getFrom(),
        fetchGroupedDataRequest.getTo(),
        fetchGroupedDataRequest.isOnlySolvedAlerts());

    QueryDto queryDto = QueryDto.builder()
        .query(query)
        .build();

    QueryResultDto queryResultDto = opendistroElasticClient.executeSql(queryDto);
    return asFetchGroupedDataResponse(queryResultDto);
  }

  private List<String> filterOutNonExistingFields(List<String> indexes, List<String> fields) {
    List<String> allAvailableFields = indexes.stream()
        .flatMap(index -> queryIndexService.getFieldsList(index).stream())
        .collect(toList());

    List<String> filteredFields = new ArrayList<>(fields);
    filteredFields.retainAll(allAvailableFields);
    return filteredFields;
  }

  private FetchGroupedDataResponse asFetchGroupedDataResponse(QueryResultDto queryResultDto) {
    List<SchemaEntry> schema = queryResultDto.getSchema();

    List<Row> rows = queryResultDto.getDatarows().stream()
        .map(dataRow -> mapDataRow(schema, dataRow))
        .collect(toList());

    return FetchGroupedDataResponse.builder()
        .rows(rows)
        .build();
  }

  private Row mapDataRow(List<SchemaEntry> schema, List<Object> dataRow) {
    if (schema.size() != dataRow.size()) {
      throw new GroupingQueryException("Invalid response structure.");
    }

    Map<String, String> map = new HashMap<>();
    Iterator<SchemaEntry> schemaIterator = schema.iterator();
    Iterator<Object> dataRowIterator = dataRow.iterator();

    while (schemaIterator.hasNext() && dataRowIterator.hasNext()) {
      String key = schemaIterator.next().getName();
      String value = ofNullable(dataRowIterator.next())
          .map(Object::toString)
          .orElse(EMPTY_VALUE_PLACEHOLDER);
      map.put(key, value);
    }

    return FetchGroupedDataResponse.Row.builder()
        .data(map)
        .count(parseInt(map.get(KEY_COUNT)))
        .build();
  }
}
