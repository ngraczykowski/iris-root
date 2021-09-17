package com.silenteight.warehouse.indexer.query.single;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.alert.ElasticsearchProperties;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.validation.Valid;

@RequiredArgsConstructor
public class ProductionSearchRequestBuilder {

  @NonNull
  @Valid
  private final ElasticsearchProperties elasticsearchProperties;

  public SearchRequest buildProductionSearchRequest(SearchSourceBuilder sourceBuilder) {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(sourceBuilder);
    searchRequest.indices(elasticsearchProperties.getProductionQueryIndex());
    return searchRequest;
  }
}
