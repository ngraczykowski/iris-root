package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@RequiredArgsConstructor
public class AlertSearchService {

  List<Map<String, Object>> searchForAlerts(
      RestHighLevelClient restHighLevelClient, SearchRequest searchRequest) {

    try {
      return doSearchAlerts(restHighLevelClient, searchRequest);
    } catch (IOException e) {
      throw new ElasticsearchException("Getting search results from Elasticsearch failed", e);
    }
  }

  private static List<Map<String, Object>> doSearchAlerts(
      RestHighLevelClient restHighLevelClient, SearchRequest searchRequest) throws IOException {

    SearchResponse search = restHighLevelClient.search(searchRequest, DEFAULT);
    SearchHit[] hits = search.getHits().getHits();
    return stream(hits)
        .map(SearchHit::getSourceAsMap)
        .collect(toList());
  }
}
