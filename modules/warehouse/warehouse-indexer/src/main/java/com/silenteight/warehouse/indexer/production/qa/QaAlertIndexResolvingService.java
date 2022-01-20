package com.silenteight.warehouse.indexer.production.qa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;
import com.silenteight.warehouse.indexer.production.qa.indextracking.IndexDiscriminatorDto;
import com.silenteight.warehouse.indexer.production.qa.indextracking.QaAlertWithIndex;
import com.silenteight.warehouse.indexer.query.single.*;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@RequiredArgsConstructor
public class QaAlertIndexResolvingService {

  private static final String[] FETCHED_FIELDS = new String[] { ALERT_NAME, DISCRIMINATOR };
  @NonNull
  AlertQueryService alertQueryService;
  @NonNull
  RestHighLevelClient restHighLevelAdminClient;
  @NonNull
  ProductionSearchRequestBuilder productionSearchRequestBuilder;

  public List<QaAlertWithIndex> getIndex(List<QaAlert> alerts) {
    List<String> alertNames = extractAlertNames(alerts);
    SearchHit[] searchHits = doSearchHits(restHighLevelAdminClient, getSearchRequest(alertNames));
    return toQaAlertWithIndex(alerts, toIndexDiscriminatorDtoMap(searchHits));
  }

  private List<String> extractAlertNames(List<QaAlert> alerts) {
    return alerts.stream()
        .map(QaAlert::getName)
        .collect(toList());
  }

  private Map<String, IndexDiscriminatorDto> toIndexDiscriminatorDtoMap(SearchHit[] searchHits) {
    return Arrays.stream(searchHits)
        .collect(toMap(
            searchHit -> searchHit.getSourceAsMap().get(ALERT_NAME).toString(),
            this::toIndexDiscriminatorDto
        ));
  }

  private IndexDiscriminatorDto toIndexDiscriminatorDto(SearchHit searchHit) {
    return IndexDiscriminatorDto.builder()
        .indexName(searchHit.getIndex())
        .discriminator(searchHit.getSourceAsMap().get(DISCRIMINATOR).toString())
        .build();
  }

  private static SearchHit[] doSearchHits(
      RestHighLevelClient restHighLevelAdminClient, SearchRequest searchRequest) {

    try {
      SearchResponse search = restHighLevelAdminClient.search(searchRequest, DEFAULT);
      return search.getHits().getHits();
    } catch (IOException e) {
      throw new RuntimeException("No alerts found");
    }
  }

  private SearchRequest getSearchRequest(List<String> alertNames) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.fetchSource(FETCHED_FIELDS, null);
    BoolQueryBuilder boolQueryBuilder = getExactMatches(alertNames);
    sourceBuilder.query(boolQueryBuilder);
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  private static BoolQueryBuilder getExactMatches(List<String> values) {
    return boolQuery().must(termsQuery(OpendistroUtils.getRawField(
        com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME), values));
  }

  private QaAlertWithIndex toQaAlertWithIndex(QaAlert alert,
      IndexDiscriminatorDto indexDiscriminatorDto) {

    return QaAlertWithIndex.builder()
        .alert(alert)
        .discriminator(indexDiscriminatorDto.getDiscriminator())
        .indexName(indexDiscriminatorDto.getIndexName())
        .build();
  }

  private List<QaAlertWithIndex> toQaAlertWithIndex(List<QaAlert> alerts,
      Map<String, IndexDiscriminatorDto> indexedMap) {

    return alerts
        .stream()
        .map(alert -> toQaAlertWithIndex(alert, indexedMap.get(alert.getName())))
        .collect(toList());
  }
}
