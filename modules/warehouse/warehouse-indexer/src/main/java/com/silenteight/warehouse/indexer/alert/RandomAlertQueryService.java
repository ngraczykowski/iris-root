package com.silenteight.warehouse.indexer.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_ID_KEY;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.functionScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;

@RequiredArgsConstructor
public class RandomAlertQueryService {

  private static final String[] ALERTS_IDS_LIST = new String[] { ALERT_ID_KEY };
  private static final String ALERT_SUFFIX_SEARCH = ".keyword";

  @NonNull
  private final AlertSearchService alertSearchService;

  @NonNull
  private final RestHighLevelClient restHighLevelAdminClient;

  @NonNull
  @Valid
  private final ProductionSearchRequestBuilder productionSearchRequestBuilder;

  public List<String> getRandomAlertIdByCriteria(AlertSearchCriteria criteria) {
    SearchRequest searchRequest =
        buildSearchRequestForRandomAlerts(
            criteria.getTimeRangeFrom(), criteria.getTimeRangeTo(), criteria.getFilter(),
            criteria.getAlertLimit());

    List<Map<String, Object>> maps =
        alertSearchService.searchForAlerts(restHighLevelAdminClient, searchRequest);

    return maps.stream()
        .map(alert -> alert.get(ALERT_ID_KEY).toString())
        .distinct()
        .collect(toList());
  }

  private SearchRequest buildSearchRequestForRandomAlerts(
      String timeRangeFrom, String timeRangeTo, Map<String, String> filter, int alertLimit) {

    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

    QueryBuilder queryForManyFieldsAndTimeRange =
        buildQueryForManyFieldsAndTimeRange(timeRangeFrom, timeRangeTo, filter);

    sourceBuilder.query(queryForManyFieldsAndTimeRange);
    sourceBuilder.fetchSource(ALERTS_IDS_LIST, null);
    sourceBuilder.size(alertLimit);

    FunctionScoreQueryBuilder functionScoreQueryBuilder =
        buildQueryForRandomScore(queryForManyFieldsAndTimeRange);

    sourceBuilder.query(functionScoreQueryBuilder);
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  private static BoolQueryBuilder buildQueryForManyFieldsAndTimeRange(
      String timeRangeFrom, String timeRangeTo,
      Map<String, String> fields) {

    BoolQueryBuilder query = new BoolQueryBuilder();

    buildQueryForManyFields(fields, query);
    query.must(buildQueryForTimeRange(timeRangeFrom, timeRangeTo));
    return query;
  }

  private static FunctionScoreQueryBuilder buildQueryForRandomScore(QueryBuilder query) {
    return functionScoreQuery(query, randomFunction());
  }

  private static void buildQueryForManyFields(
      Map<String, String> fields, BoolQueryBuilder query) {

    fields.forEach((key, value) -> query.must(getExactMatch(key, value)));
  }

  static MatchQueryBuilder getExactMatch(String fieldName, String value) {
    return new MatchQueryBuilder(OpendistroUtils.getRawField(fieldName), value);
  }

  private static BoolQueryBuilder buildQueryForTimeRange(
      String timeFrom, String timeTo) {

    return boolQuery()
        .must(rangeQuery(INDEX_TIMESTAMP).gte(timeFrom).lte(timeTo));
  }
}
