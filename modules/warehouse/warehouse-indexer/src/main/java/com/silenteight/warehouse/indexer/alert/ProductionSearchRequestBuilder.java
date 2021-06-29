package com.silenteight.warehouse.indexer.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.validation.Valid;

@RequiredArgsConstructor
class ProductionSearchRequestBuilder {

  @NonNull
  @Valid
  private final ElasticsearchProperties elasticsearchProperties;

  SearchRequest buildProductionSearchRequest(SearchSourceBuilder sourceBuilder) {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(sourceBuilder);
    searchRequest.indices(elasticsearchProperties.getProductionQueryIndex());
    return searchRequest;
  }
}
