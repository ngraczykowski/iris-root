package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Map;

@RequiredArgsConstructor
public class SimpleElasticTestClient {

  private final RestHighLevelClient restHighLevelClient;

  @SneakyThrows
  public Map<String,Object> getSource(String index, String documentId) {
    GetRequest getRequest = new GetRequest(index, documentId);
    GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    return getResponse.getSource();
  }
}
