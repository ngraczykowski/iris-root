package com.silenteight.warehouse.common.elastic;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

@RequiredArgsConstructor
class RestHighLevelElasticClientFactory {

  private final RestClientBuilder restClientBuilder;

  RestHighLevelClient getAdminClient() {
    return new RestHighLevelClient(restClientBuilder);
  }
}
