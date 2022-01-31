package com.silenteight.warehouse.indexer.query.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.QueryDto;
import com.silenteight.warehouse.common.opendistro.elastic.SearchResultDto;
import com.silenteight.warehouse.common.opendistro.elastic.SearchResultDto.Bucket;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.index.FieldsQueryIndexService;
import com.silenteight.warehouse.indexer.query.sql.MultiValueCondition;
import com.silenteight.warehouse.indexer.query.sql.SingleValueCondition;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class GroupingQueryElasticSearchService implements GroupingQueryService {

  static final String EMPTY_VALUE_PLACEHOLDER = "";

  @NonNull
  private final SqlBuilder sqlBuilder;
  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;
  @NonNull
  private final FieldsQueryIndexService fieldsQueryIndexService;

  @Override
  public FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest) {

    log.debug("Elastic GroupingQueryService: {}", fetchGroupedDataRequest);

    SafetyQueryProcessor safetyQueryProcessor =
        new SafetyQueryProcessor(fetchGroupedDataRequest.getIndexes(), fieldsQueryIndexService);

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

    SearchResultDto searchResultDto = opendistroElasticClient.executeGroupingSearch(
        queryDto, fetchGroupedDataRequest.getIndexes().get(0));

    return asFetchGroupedDataResponse(searchResultDto);
  }

  private FetchGroupedDataResponse asFetchGroupedDataResponse(SearchResultDto searchResultDto) {

    List<Row> rows = searchResultDto
        .getBuckets()
        .stream()
        .map(this::toRow)
        .collect(toList());

    return FetchGroupedDataResponse.builder()
        .rows(rows)
        .build();
  }

  private Row toRow(Bucket bucket) {
    return FetchGroupedDataResponse.Row.builder()
        .data(bucket.getKey())
        .count(bucket.getDocCount())
        .build();
  }
}
