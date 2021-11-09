package com.silenteight.warehouse.retention.production.personalinformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.*;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@RequiredArgsConstructor
public class AlertDiscriminatorResolvingService {

  private static final String[] FETCHED_FIELDS = new String[] { DISCRIMINATOR };

  @NonNull
  private final RestHighLevelClient restHighLevelAdminClient;
  @NonNull
  private final ProductionSearchRequestBuilder productionSearchRequestBuilder;
  @NonNull
  private final AlertSearchService alertSearchService;

  public List<String> getDiscriminatorsForAlertNames(List<String> alertNames) {
    List<Map<String, Object>> maps =
        alertSearchService.searchForAlerts(restHighLevelAdminClient,  getSearchRequest(alertNames));

    return maps.stream()
        .map(alert -> alert.get(DISCRIMINATOR).toString())
        .distinct()
        .collect(toList());
  }

  private SearchRequest getSearchRequest(List<String> alertNames) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.fetchSource(FETCHED_FIELDS, null);
    BoolQueryBuilder boolQueryBuilder = getExactMatches(alertNames);
    sourceBuilder.query(boolQueryBuilder);
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  private static BoolQueryBuilder getExactMatches(List<String> values) {
    return boolQuery().should(termsQuery(OpendistroUtils.getRawField(ALERT_NAME), values));
  }
}