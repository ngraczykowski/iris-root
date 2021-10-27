package com.silenteight.warehouse.indexer.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;
import com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils;
import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.alert.mapping.AlertDefinition;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.match.mapping.MatchDefinition;
import com.silenteight.warehouse.indexer.match.mapping.MatchMapper;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static java.util.Collections.emptyMap;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertMappingService {

  private static final String DELIMINATOR = "_";
  private static final int SINGLE_MATCH_ALERT = 1;
  private static final int NO_MATCH_ALERT = 0;

  @NonNull
  private final AlertMapper alertMapper;

  @NonNull
  private final MatchMapper matchMapper;

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

  private MapWithIndex mapFields(Alert alert, String targetIndexName) {
    String alertName = alert.getName();

    try {
      if (isNull(alertName) || alertName.isBlank())
        throw new IllegalArgumentException("alertName not set in simulation request.");

      Map<String, Object> productionData = getAlertFromProductionIndex(alertName);
      Map<String, Object> simulationAlertData =
          alertMapper.convertAlertToAttributes(toAlertDefinition(alert));
      Map<String, Object> simulationData = getSimulationData(alert, simulationAlertData);

      Map<String, Object> payload = new HashMap<>();
      payload.putAll(productionData);
      payload.putAll(simulationData);

      return new MapWithIndex(targetIndexName, alertName, payload);
    } catch (RuntimeException e) {
      log.warn("Storing production data in simulation index failed, alertName=" + alertName, e);
      return null;
    }
  }

  private Map<String, Object> getSimulationData(
      Alert alert, Map<String, Object> simulationAlertData) {

    if (alert.getMatchesCount() > SINGLE_MATCH_ALERT) {
      log.warn("Simulation handles only single match alerts. "
                   + "Received alert with {} matches. "
                   + "Will process only the first one.",
               alert.getMatchesCount());
    }
    if (alert.getMatchesCount() == NO_MATCH_ALERT) {
      log.warn("Simulation handles only single match alerts. Received alert with no matches.");
      return simulationAlertData;
    }

    return matchMapper.convertMatchToAttributes(
        simulationAlertData, toMatchDefinition(alert.getMatches(0)));
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

  private static MatchDefinition toMatchDefinition(Match match) {
    return MatchDefinition.builder()
        .name(match.getName())
        .payload(flattenPayload(match.getPayload()))
        .build();
  }

  @NotNull
  private static Struct flattenPayload(Struct payload) {
    return Struct.newBuilder().putAllFields(createFlattenMap(payload)).build();
  }

  @NotNull
  private static Map<String, Value> createFlattenMap(Struct payload) {
    return payload
        .getFieldsMap()
        .entrySet()
        .stream()
        .map(entry -> flattenEntry(entry.getKey(), entry.getValue()))
        .flatMap(Collection::stream)
        .collect(toMap(Element::getKey, Element::getValue));
  }

  private static List<Element> flattenEntry(String key, Value value) {
    if (value.hasStructValue())
      return flattenStruct(value.getStructValue(), key);

    return of(Element.of(key, value));
  }

  @NotNull
  private static List<Element> flattenStruct(Struct struct, String key) {
    return struct
        .getFieldsMap()
        .entrySet()
        .stream()
        .map(entry -> Element.of(buildFlattenKey(key, entry), entry.getValue()))
        .collect(toList());
  }

  @NotNull
  private static String buildFlattenKey(String key, Entry<String, Value> entry) {
    return key + DELIMINATOR + entry.getKey();
  }

  @lombok.Value(staticConstructor = "of")
  private static class Element {

    String key;
    Value value;
  }
}
