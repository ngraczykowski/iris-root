package com.silenteight.warehouse.indexer.query.streaming;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.validation.Valid;

import static java.util.List.of;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;

@Slf4j
@RequiredArgsConstructor
class ScrollSearchStreamingService implements DataProvider {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;
  @Valid
  @NonNull
  private final ScrollSearchProperties scrollSearchProperties;
  @NonNull
  private final ScrollSearchQueryBuilder scrollSearchQueryBuilder;
  @NonNull
  private final DataResponseParser responseParser;

  @Override
  public void fetchData(FetchDataRequest request, Consumer<Collection<String>> consumer) {
    try {
      log.debug("Elastic ScrollSearchStreamingService: {}", request);
      doFetchData(request, consumer);
    } catch (IOException e) {
      throw new ReportGenerationException(
          request.getName(), "Error while streaming data from ES to Consumer", e);
    }
  }

  private void doFetchData(
      FetchDataRequest request, Consumer<Collection<String>> consumer) throws IOException {

    final Scroll scroll =
        new Scroll(timeValueSeconds(scrollSearchProperties.getKeepAliveSeconds()));

    QueryBuilder query = scrollSearchQueryBuilder.buildQuery(request);
    SearchResponse searchResponse = executeSearch(request, scroll, query);
    writeCsv(scroll, searchResponse, request.getFields(), consumer);
  }

  private SearchResponse executeSearch(
      FetchDataRequest request, Scroll scroll, QueryBuilder query) throws IOException {

    SearchSourceBuilder searchSourceBuilder = toSearchSourceBuilder(request, query);
    SearchRequest searchRequest = toSearchRequest(request, scroll, searchSourceBuilder);
    return restHighLevelClient.search(searchRequest, DEFAULT);
  }

  private SearchSourceBuilder toSearchSourceBuilder(FetchDataRequest request, QueryBuilder query) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(query);
    searchSourceBuilder.size(scrollSearchProperties.getBatchSize());
    searchSourceBuilder.fetchSource(request.getFieldsArray(), null);
    return searchSourceBuilder;
  }

  private static SearchRequest toSearchRequest(
      FetchDataRequest request, Scroll scroll, SearchSourceBuilder searchSourceBuilder) {

    SearchRequest searchRequest = new SearchRequest(request.getIndexesArray());
    searchRequest.scroll(scroll);
    searchRequest.source(searchSourceBuilder);
    return searchRequest;
  }

  private void writeCsv(
      Scroll scroll,
      SearchResponse searchResponse,
      List<String> fieldNames,
      Consumer<Collection<String>> consumer) throws IOException {

    writeHeader(fieldNames, consumer);
    String scrollId = writeBody(scroll, searchResponse, fieldNames, consumer);
    cleanUp(scrollId);
  }

  private void writeHeader(List<String> fieldNames, Consumer<Collection<String>> consumer) {
    String labels = responseParser.parseLabels(fieldNames);
    consumer.accept(of(labels));
  }

  private String writeBody(
      Scroll scroll, SearchResponse response, List<String> fieldNames,
      Consumer<Collection<String>> consumer) throws IOException {

    SearchHit[] searchHits = writeLines(response, fieldNames, consumer);
    String scrollId = response.getScrollId();
    while (searchHits.length > 0) {
      SearchScrollRequest request = new SearchScrollRequest(scrollId);
      request.scroll(scroll);
      response = restHighLevelClient.scroll(request, DEFAULT);
      scrollId = response.getScrollId();
      searchHits = writeLines(response, fieldNames, consumer);
    }
    return scrollId;
  }

  private SearchHit[] writeLines(
      SearchResponse searchResponse, List<String> fieldNames,
      Consumer<Collection<String>> consumer) {

    SearchHit[] searchHits = searchResponse.getHits().getHits();
    List<String> lines = responseParser.parseValues(searchHits, fieldNames);
    consumer.accept(lines);
    return searchHits;
  }

  private void cleanUp(String scrollId) throws IOException {
    ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
    clearScrollRequest.addScrollId(scrollId);
    ClearScrollResponse response = restHighLevelClient.clearScroll(clearScrollRequest, DEFAULT);
    if (!response.isSucceeded())
      log.warn("Something went wrong with clearing Scroll with scrollId={}", scrollId);
  }
}
