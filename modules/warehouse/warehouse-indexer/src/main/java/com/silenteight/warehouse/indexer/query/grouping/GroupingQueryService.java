package com.silenteight.warehouse.indexer.query.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.QueryDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto.SchemaEntry;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.query.grouping.SqlBuilder.KEY_COUNT;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class GroupingQueryService {

  @NonNull
  private final SqlBuilder sqlBuilder;
  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;

  public FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest) {

    String query = sqlBuilder.groupByBetweenDates(
        fetchGroupedDataRequest.getIndexes(),
        fetchGroupedDataRequest.getFields(),
        fetchGroupedDataRequest.getDateField(),
        fetchGroupedDataRequest.getFrom(),
        fetchGroupedDataRequest.getTo());

    QueryDto queryDto = QueryDto.builder()
        .query(query)
        .build();

    QueryResultDto queryResultDto = opendistroElasticClient.executeSql(queryDto);
    return asFetchGroupedDataResponse(queryResultDto);
  }

  FetchGroupedDataResponse asFetchGroupedDataResponse(QueryResultDto queryResultDto) {
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
          .orElse(null);
      map.put(key, value);
    }

    return FetchGroupedDataResponse.Row.builder()
        .data(map)
        .count(parseInt(map.get(KEY_COUNT)))
        .build();
  }
}
