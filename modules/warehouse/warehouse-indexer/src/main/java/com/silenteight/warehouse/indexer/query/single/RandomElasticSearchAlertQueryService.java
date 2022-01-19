package com.silenteight.warehouse.indexer.query.single;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.MultiValueEntry;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils.getRawField;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;

@RequiredArgsConstructor
public class RandomElasticSearchAlertQueryService implements RandomAlertService {

  private static final String[] ALERTS_IDS_LIST = new String[] { ALERT_NAME };

  @NonNull
  private final AlertSearchService alertSearchService;

  @NonNull
  private final RestHighLevelClient restHighLevelAdminClient;

  @NonNull
  @Valid
  private final ProductionSearchRequestBuilder productionSearchRequestBuilder;

  @Override
  public List<String> getRandomDiscriminatorByCriteria(AlertSearchCriteria criteria) {
    SearchRequest searchRequest = buildSearchRequestForRandomAlerts(criteria);

    List<Map<String, Object>> maps =
        alertSearchService.searchForDocuments(restHighLevelAdminClient, searchRequest);

    return maps.stream()
        .map(alert -> alert.get(ALERT_NAME).toString())
        .distinct()
        .collect(toList());
  }

  private SearchRequest buildSearchRequestForRandomAlerts(AlertSearchCriteria criteria) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.query(buildQueryWithRandomScore(criteria));
    sourceBuilder.fetchSource(ALERTS_IDS_LIST, null);
    sourceBuilder.size(criteria.getAlertLimit());
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  private QueryBuilder buildQueryWithRandomScore(AlertSearchCriteria criteria) {
    BoolQueryBuilder filterQuery = buildQueryForAttributeFiltering(criteria.getFilter());
    BoolQueryBuilder timeRangeQuery = buildQueryForTimeRange(
        criteria.getTimeFieldName(),
        criteria.getTimeRangeFrom(),
        criteria.getTimeRangeTo());

    QueryBuilder query = boolQuery()
        .must(filterQuery)
        .must(timeRangeQuery);

    return functionScoreQuery(query, randomFunction());
  }

  private static BoolQueryBuilder buildQueryForAttributeFiltering(List<MultiValueEntry> entries) {
    return entries.stream()
        .map(e -> boolQuery().must(termsQuery(getRawField(e.getField()), e.getValues())))
        .reduce(boolQuery(), BoolQueryBuilder::must);
  }

  private static BoolQueryBuilder buildQueryForTimeRange(
      String timeField, String timeFrom, String timeTo) {
    return boolQuery().must(rangeQuery(timeField).gte(timeFrom).lte(timeTo));
  }
}
