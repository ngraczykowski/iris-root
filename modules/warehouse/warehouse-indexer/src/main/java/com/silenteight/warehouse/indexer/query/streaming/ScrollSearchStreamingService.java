package com.silenteight.warehouse.indexer.query.streaming;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.streaming.exception.FetchAllDataException;

import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils.getRawField;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Slf4j
@RequiredArgsConstructor
class ScrollSearchStreamingService implements AllDataProvider {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;
  @Valid
  @NonNull
  private final ScrollSearchProperties scrollSearchProperties;

  @Override
  public void fetchData(
      FetchAllDataRequest request, Consumer<Collection<FetchedDocument>> consumer) {
    try {
      log.debug("Elastic ScrollSearchStreamingService: {}", request);
      doFetchData(request, consumer);
    } catch (IOException e) {
      throw new FetchAllDataException(e);
    }
  }

  private void doFetchData(
      FetchAllDataRequest request,
      Consumer<Collection<FetchedDocument>> consumer) throws IOException {

    SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder(request);

    ScrollManager scrollManager = new ScrollManager(
        restHighLevelClient,
        searchSourceBuilder,
        request.getIndexesArray(),
        scrollSearchProperties.getKeepAliveSeconds());

    while (scrollManager.hasData()) {
      consumer.accept(scrollManager.fetchDocuments());
    }
    scrollManager.cleanUp();
  }

  @NotNull
  private SearchSourceBuilder getSearchSourceBuilder(FetchAllDataRequest request) {
    List<QueryFilter> queryFilters = request.getQueryFilters();
    BoolQueryBuilder query = boolQuery();
    queryFilters.forEach(queryFilter -> query.must(buildTermsQuery(queryFilter)));

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(query);
    searchSourceBuilder.size(scrollSearchProperties.getBatchSize());
    searchSourceBuilder.fetchSource(request.getFieldsNamesArray(), null);
    return searchSourceBuilder;
  }

  private static TermsQueryBuilder buildTermsQuery(QueryFilter queryFilters) {
    return termsQuery(getRawField(queryFilters.getField()), queryFilters.getAllowedValues());
  }

  @RequiredArgsConstructor
  private static class ScrollManager {

    private final RestHighLevelClient restHighLevelClient;
    private final SearchSourceBuilder searchSourceBuilder;
    private final String[] indexesArray;
    private final Integer keepAliveSeconds;
    private boolean firstRun = true;
    private String scrollId;
    private Scroll scroll;

    boolean hasData() {
      return firstRun || !ofNullable(scrollId).map(String::isBlank).orElse(true);
    }

    Collection<FetchedDocument> fetchDocuments() {
      if (firstRun)
        return firstRun();
      else
        return doScroll();
    }

    @SneakyThrows
    private List<FetchedDocument> firstRun() {
      firstRun = false;
      scroll = new Scroll(timeValueSeconds(keepAliveSeconds));

      SearchRequest searchRequest = new SearchRequest(indexesArray);
      searchRequest.scroll(scroll);
      searchRequest.source(searchSourceBuilder);
      SearchResponse searchResponse = restHighLevelClient.search(searchRequest, DEFAULT);
      scrollId = searchResponse.getScrollId();
      return toDocument(searchResponse);
    }

    @SneakyThrows
    private List<FetchedDocument> doScroll() {
      SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
      scrollRequest.scroll(scroll);
      SearchResponse searchResponse = restHighLevelClient.scroll(scrollRequest, DEFAULT);
      scrollId = searchResponse.getScrollId();
      return toDocument(searchResponse);
    }

    private static List<FetchedDocument> toDocument(SearchResponse searchResponse) {
      return stream(searchResponse.getHits().getHits())
          .map(SearchHit::getSourceAsMap)
          .map(FetchedDocument::new)
          .collect(toList());
    }

    @SneakyThrows
    void cleanUp() {
      ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
      clearScrollRequest.addScrollId(scrollId);
      ClearScrollResponse response = restHighLevelClient.clearScroll(clearScrollRequest, DEFAULT);
      if (!response.isSucceeded())
        log.warn("Something went wrong with clearing Scroll with scrollId={}", scrollId);
    }
  }
}
