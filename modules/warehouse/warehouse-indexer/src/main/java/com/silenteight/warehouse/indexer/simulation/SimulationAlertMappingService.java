package com.silenteight.warehouse.indexer.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;
import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.alert.mapping.AlertDefinition;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertMappingService {

  @NonNull
  private final AlertMapper alertMapper;

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertSearchService alertSearchService;

  @NonNull
  ProductionSearchRequestBuilder productionSearchRequestBuilder;

  List<MapWithIndex> mapFields(List<Alert> alerts, String targetIndexName) {
    return alerts.stream()
        .map(alert -> mapFields(alert, targetIndexName))
        .filter(Objects::nonNull)
        .collect(toList());
  }

  MapWithIndex mapFields(Alert alert, String targetIndexName) {
    String alertName = alert.getName();

    try {
      if (isNull(alertName) || alertName.isBlank())
        throw new IllegalArgumentException("alertName not set in simulation request.");

      Map<String, Object> productionData = getAlertFromProductionIndex(alertName);
      Map<String, Object> simulationData =
          alertMapper.convertAlertToAttributes(toAlertDefinition(alert));

      HashMap<String, Object> payload = new HashMap<>();
      payload.putAll(productionData);
      payload.putAll(simulationData);

      return new MapWithIndex(targetIndexName, alertName, payload);
    } catch (RuntimeException e) {
      log.warn("Storing production data in simulation index failed, alertName=" + alertName, e);
      return null;
    }
  }

  private Map<String, Object> getAlertFromProductionIndex(String alertName) {
    try {
      SearchRequest searchRequest = buildSearchRequestForSpecificField(ALERT_NAME, alertName);
      return alertSearchService.searchForAlerts(restHighLevelClient, searchRequest).stream()
          .findFirst()
          .orElseGet(() -> getDefaultProductionData(alertName));
    } catch (RuntimeException e) {
      log.warn("Getting production data for simulation failed, alertName=" + alertName, e);
      return getDefaultProductionData(alertName);
    }
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

  private static AlertDefinition toAlertDefinition(Alert alert) {
    return AlertDefinition.builder()
        .discriminator(alert.getDiscriminator())
        .name(alert.getName())
        .payload(alert.getPayload())
        .build();
  }
}
