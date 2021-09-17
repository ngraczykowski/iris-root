package com.silenteight.warehouse.indexer.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_NAME;
import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@RequiredArgsConstructor
@Slf4j
public class AlertCopyDataService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;
  @NonNull
  private final AlertSearchService alertSearchService;
  @NonNull
  ProductionSearchRequestBuilder productionSearchRequestBuilder;

  public void copyProductionIntoSimulation(List<Alert> alerts, String targetSimulationIndex) {
    alerts.forEach(alert -> copyProductionIntoSimulation(alert, targetSimulationIndex));
  }

  public void copyProductionIntoSimulation(Alert alert, String targetSimulationIndex) {
    String alertName = alert.getName();
    try {
      if (isNull(alertName) || alertName.isBlank())
        throw new IllegalArgumentException("alertName not set in simulation request.");

      Map<String, Object> productionDocument = getAlertFromProductionIndex(alertName);
      storeAlertInSimulationIndex(targetSimulationIndex, alertName, productionDocument);
    } catch (RuntimeException e) {
      log.warn("Storing production data in simulation index failed, alertName=" + alertName, e);
    }
  }

  private Map<String, Object> getAlertFromProductionIndex(String alertName) {
    SearchRequest searchRequest = buildSearchRequestForSpecificField(ALERT_NAME, alertName);
    return alertSearchService.searchForAlerts(restHighLevelClient, searchRequest).stream()
        .findFirst()
        .orElseGet(() -> getDefaultProductionData(alertName));
  }

  private Map<String, Object> getDefaultProductionData(String alertName) {
    log.warn("Production data not available for simulation, alertName={}", alertName);
    return emptyMap();
  }

  private SearchRequest buildSearchRequestForSpecificField(
      String requestedField, String requestedValue) {

    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    MatchQueryBuilder matchQueryBuilder = getExactMatch(requestedField, requestedValue);
    sourceBuilder.query(matchQueryBuilder);
    return productionSearchRequestBuilder.buildProductionSearchRequest(sourceBuilder);
  }

  static MatchQueryBuilder getExactMatch(String fieldName, String value) {
    return new MatchQueryBuilder(OpendistroUtils.getRawField(fieldName), value);
  }

  @SneakyThrows
  private void storeAlertInSimulationIndex(
      String targetSimulationIndex, String alertName, Map<String, Object> document) {

    UpdateRequest updateRequest = new UpdateRequest(targetSimulationIndex, alertName);
    updateRequest.doc(document);
    updateRequest.docAsUpsert(true);

    restHighLevelClient.update(updateRequest, DEFAULT);
  }
}
