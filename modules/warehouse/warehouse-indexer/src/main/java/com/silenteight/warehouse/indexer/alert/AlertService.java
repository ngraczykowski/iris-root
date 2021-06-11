package com.silenteight.warehouse.indexer.alert;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;
import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.validation.Valid;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_ID_KEY;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.NameResource.getSplitName;
import static java.lang.String.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@AllArgsConstructor
public class AlertService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertMapper alertMapper;

  @NonNull
  @Valid
  private final ElasticsearchProperties elasticsearchProperties;

  public void indexAlerts(List<Alert> alerts, String indexName) {
    BulkRequest bulkRequest = new BulkRequest();

    alerts.stream()
        .flatMap(alert -> convertAlertToDocument(indexName, alert))
        .forEach(bulkRequest::add);

    attemptToSaveAlert(bulkRequest);
  }

  public AlertAttributes getSingleAlertAttributes(List<String> fields, String id) {
    Map<String, Object> singleAlert = searchForAlert(id).stream()
        .findFirst()
        .orElseThrow(
            () -> new AlertNotFoundException(format("Alert with %s id not found.", id)));

    return AlertAttributes.builder()
        .attributes(convertSingleAlertToAttributes(fields, singleAlert))
        .build();
  }

  public AlertsAttributesListDto getMultipleAlertsAttributes(
      List<String> fields, List<String> idList) {

    List<String> splitIdList = idList.stream()
        .map(NameResource::getSplitName)
        .collect(toList());

    List<AlertAttributes> alertAttributesList = splitIdList.stream()
        .map(alertId -> getSingleAlertAttributes(fields, alertId))
        .collect(toList());

    return AlertsAttributesListDto.builder()
        .alerts(alertAttributesList)
        .build();
  }

  private Stream<IndexRequest> convertAlertToDocument(String indexName, Alert alert) {
    List<Match> matchesList = alert.getMatchesList();
    if (matchesList.isEmpty()) {
      throw new ZeroMatchesException("There are no matches in this alert.");
    }
    return matchesList.stream()
        .map(match -> prepareDocument(alert, indexName, match));
  }

  private IndexRequest prepareDocument(Alert alert, String indexName, Match match) {
    IndexRequest indexRequest = new IndexRequest(indexName);
    indexRequest.id(getSplitName(alert.getName() + ":" + getSplitName(match.getName())));
    indexRequest.source(alertMapper.convertAlertAndMatchToAttributes(alert, match));
    return indexRequest;
  }

  private void attemptToSaveAlert(BulkRequest indexRequest) {
    try {
      restHighLevelClient.bulk(indexRequest, DEFAULT);
    } catch (IOException e) {
      throw new AlertNotIndexedExceptions("Alert has not been indexed", e);
    }
  }

  private Map<String, String> convertSingleAlertToAttributes(
      List<String> requestedFields, Map<String, Object> alertAttributes) {

    Map<String, String> requestedAlertAttributes = new HashMap<>();

    requestedFields.forEach(
        attribute -> convertSingleAttributeToMap(
            alertAttributes, requestedAlertAttributes, attribute));
    return requestedAlertAttributes;
  }

  private void convertSingleAttributeToMap(
      Map<String, Object> alert, Map<String, String> alertAttributes, String requestedAttribute) {

    String prefixedAttributeKey = ALERT_PREFIX + requestedAttribute;
    String attributeValue = ofNullable(alert.get(prefixedAttributeKey))
        .map(Object::toString)
        .orElse(null);
    alertAttributes.put(requestedAttribute, attributeValue);
  }

  private List<Map<String, Object>> searchForAlert(String alertId) {
    SearchRequest searchRequest = buildSearchRequest(alertId);

    try {
      SearchResponse search = restHighLevelClient.search(searchRequest, DEFAULT);
      SearchHit[] hits = search.getHits().getHits();
      return Arrays.stream(hits)
          .map(SearchHit::getSourceAsMap)
          .collect(toList());
    } catch (IOException e) {
      throw new ElasticsearchException("Getting search results from Elasticsearch failed", e);
    }
  }

  private SearchRequest buildSearchRequest(String alertId) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(ALERT_ID_KEY, alertId);
    sourceBuilder.query(matchQueryBuilder);
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(sourceBuilder);
    searchRequest.indices(elasticsearchProperties.getProductionQueryIndex());
    return searchRequest;
  }
}
