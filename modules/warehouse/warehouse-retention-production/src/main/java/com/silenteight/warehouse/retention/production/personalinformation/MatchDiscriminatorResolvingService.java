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

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.MATCH_DISCRIMINATOR;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@RequiredArgsConstructor
public class MatchDiscriminatorResolvingService {

  private static final String[] FETCHED_FIELDS = new String[] { MATCH_DISCRIMINATOR };

  @NonNull
  private final RestHighLevelClient restHighLevelAdminClient;
  @NonNull
  private final ProductionSearchRequestBuilder productionMatchSearchRequestBuilder;
  @NonNull
  private final AlertSearchService alertSearchService;

  public List<String> getDiscriminatorsForAlert(String discriminator) {
    List<Map<String, Object>> maps = alertSearchService.searchForDocuments(
        restHighLevelAdminClient, getSearchRequest(discriminator));

    return maps.stream()
        .map(alert -> alert.get(MATCH_DISCRIMINATOR).toString())
        .distinct()
        .collect(toList());
  }

  private SearchRequest getSearchRequest(String discriminator) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.fetchSource(FETCHED_FIELDS, null);
    BoolQueryBuilder boolQueryBuilder = getExactMatches(discriminator);
    sourceBuilder.query(boolQueryBuilder);
    return productionMatchSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  private static BoolQueryBuilder getExactMatches(String value) {
    return boolQuery().should(termsQuery(OpendistroUtils.getRawField(DISCRIMINATOR), value));
  }
}
