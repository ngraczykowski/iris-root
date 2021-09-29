package com.silenteight.warehouse.indexer.query.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.QueryDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto.SchemaEntry;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.index.FieldsQueryIndexService;
import com.silenteight.warehouse.indexer.query.sql.MultiValueCondition;
import com.silenteight.warehouse.indexer.query.sql.SingleValueCondition;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.query.sql.SqlBuilder.KEY_COUNT;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class GroupingQueryService {

  static final String EMPTY_VALUE_PLACEHOLDER = "";

  @NonNull
  private final SqlBuilder sqlBuilder;
  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;
  @NonNull
  private final FieldsQueryIndexService queryIndexService;

  public FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest) {

    log.debug("Elastic GroupingQueryService: {}", fetchGroupedDataRequest);

    SafetyQueryProcessor safetyQueryProcessor =
        new SafetyQueryProcessor(fetchGroupedDataRequest.getIndexes(), queryIndexService);

    List<String> existingFields = safetyQueryProcessor
        .filterOutNonExistingFields(fetchGroupedDataRequest.getFields());
    List<String> existingFilterFields = safetyQueryProcessor
        .filterOutNonExistingFields(fetchGroupedDataRequest.getQueryFilterFields());
    if (existingFilterFields.size() != fetchGroupedDataRequest.getQueryFilterFields().size()) {
      log.warn("Cannot filter on non existing fields. Returning empty query result");
      return FetchGroupedDataResponse.empty();
    }

    List<MultiValueCondition> whereConditions = fetchGroupedDataRequest.getQueryFilters().stream()
        .map(filter -> new MultiValueCondition(filter.getField(), filter.getAllowedValues()))
        .collect(toList());

    SingleValueCondition fromCondition = SingleValueCondition.builder()
        .field(fetchGroupedDataRequest.getDateField())
        .value(fetchGroupedDataRequest.getFrom())
        .build();

    SingleValueCondition toCondition = SingleValueCondition.builder()
        .field(fetchGroupedDataRequest.getDateField())
        .value(fetchGroupedDataRequest.getTo())
        .build();

    String query = sqlBuilder.groupByBetweenDates(
        fetchGroupedDataRequest.getIndexes(),
        existingFields,
        whereConditions,
        fromCondition,
        toCondition);

    QueryDto queryDto = QueryDto.builder()
        .query(query)
        .build();

    QueryResultDto queryResultDto = opendistroElasticClient.executeSql(queryDto);
    return asFetchGroupedDataResponse(queryResultDto);
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
