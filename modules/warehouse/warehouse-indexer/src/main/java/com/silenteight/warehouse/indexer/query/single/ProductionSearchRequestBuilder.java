package com.silenteight.warehouse.indexer.query.single;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@RequiredArgsConstructor
public class ProductionSearchRequestBuilder {

  @NonNull
  private final String[] indices;

  public SearchRequest buildProductionSearchRequest(SearchSourceBuilder sourceBuilder) {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(sourceBuilder);
    searchRequest.indices(indices);
    return searchRequest;
  }
}
