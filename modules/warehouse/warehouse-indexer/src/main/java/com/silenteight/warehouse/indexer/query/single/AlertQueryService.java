package com.silenteight.warehouse.indexer.query.single;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@RequiredArgsConstructor
class AlertQueryService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertSearchService alertSearchService;

  @NonNull
  ProductionSearchRequestBuilder productionSearchRequestBuilder;

  public Collection<Map<String, String>> getMultipleAlertsAttributes(
      List<String> fields, List<String> discriminatorList) {

    return discriminatorList.stream()
        .map(discriminator -> getAlert(fields, discriminator))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());
  }

  public Map<String, String> getSingleAlertAttributes(List<String> fields, String discriminator) {
    return getAlert(fields, discriminator).orElseThrow(
        () -> new AlertNotFoundException(format("Alert with %s id not found.", discriminator)));
  }

  private Optional<Map<String, String>> getAlert(List<String> fields, String discriminator) {
    return searchForAlert(DISCRIMINATOR, discriminator).stream()
        .findFirst()
        .map(singleAlert -> convertSingleAlertToAttributes(fields, singleAlert));
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

    String attributeValue = ofNullable(alert.get(requestedAttribute))
        .map(Object::toString)
        .orElse(null);
    alertAttributes.put(requestedAttribute, attributeValue);
  }

  private List<Map<String, Object>> searchForAlert(String requestedField, String discriminator) {
    SearchRequest searchRequest = buildSearchRequestForSpecificField(requestedField, discriminator);
    return alertSearchService.searchForDocuments(restHighLevelClient, searchRequest);
  }

  private SearchRequest buildSearchRequestForSpecificField(
      String requestedField, String requestedValue) {

    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = getExactMatch(requestedField, requestedValue);
    sourceBuilder.query(boolQueryBuilder);
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  private static BoolQueryBuilder getExactMatch(String fieldName, String value) {
    return boolQuery().must(termsQuery(OpendistroUtils.getRawField(fieldName), value));
  }
}
