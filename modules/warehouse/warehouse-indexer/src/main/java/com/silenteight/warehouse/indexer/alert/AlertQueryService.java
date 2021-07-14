package com.silenteight.warehouse.indexer.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.RandomAlertQueryService.getExactMatch;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AlertQueryService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertSearchService alertSearchService;

  @NonNull
  ProductionSearchRequestBuilder productionSearchRequestBuilder;

  public AlertAttributes getSingleAlertAttributes(List<String> fields, String discriminator) {
    Map<String, Object> singleAlert = searchForAlert(DISCRIMINATOR, discriminator).stream()
        .findFirst()
        .orElseThrow(
            () -> new AlertNotFoundException(format("Alert with %s id not found.", discriminator)));

    return AlertAttributes.builder()
        .attributes(convertSingleAlertToAttributes(fields, singleAlert))
        .build();
  }

  public AlertsAttributesListDto getMultipleAlertsAttributes(
      List<String> fields, List<String> discriminatorList) {

    List<AlertAttributes> alertAttributesList = discriminatorList.stream()
        .map(discriminator -> getSingleAlertAttributes(fields, discriminator))
        .collect(toList());

    return AlertsAttributesListDto.builder()
        .alerts(alertAttributesList)
        .build();
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

  private List<Map<String, Object>> searchForAlert(String requestedField, String discriminator) {
    SearchRequest searchRequest = buildSearchRequestForSpecificField(requestedField, discriminator);
    return alertSearchService.searchForAlerts(restHighLevelClient, searchRequest);
  }

  private SearchRequest buildSearchRequestForSpecificField(
      String requestedField, String requestedValue) {

    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    MatchQueryBuilder matchQueryBuilder = getExactMatch(requestedField, requestedValue);
    sourceBuilder.query(matchQueryBuilder);
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }
}
