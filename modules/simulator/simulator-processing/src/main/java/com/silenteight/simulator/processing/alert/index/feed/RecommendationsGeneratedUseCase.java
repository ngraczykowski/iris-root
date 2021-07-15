package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.simulator.processing.alert.index.amqp.listener.RecommendationsGeneratedMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class RecommendationsGeneratedUseCase implements RecommendationsGeneratedMessageHandler {

  @NonNull
  private final AlertService alertService;
  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final RequestIdGenerator requestIdGenerator;

  @Override
  public SimulationDataIndexRequest handle(RecommendationsGenerated request) {
    String requestId = requestIdGenerator.generate();
    SimulationDataIndexRequest indexRequest = toIndexRequest(requestId, request);
    indexedAlertService.saveAsSent(
        requestId, request.getAnalysis(), request.getRecommendationInfosCount());
    return indexRequest;
  }

  private SimulationDataIndexRequest toIndexRequest(
      String requestId, RecommendationsGenerated request) {

    return SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(request.getAnalysis())
        .addAllAlerts(toAlertsToIndex(request.getRecommendationInfosList()))
        .build();
  }

  private Collection<Alert> toAlertsToIndex(List<RecommendationInfo> recommendations) {
    return recommendations
        .stream()
        .map(RecommendationInfo::getAlert)
        .map(alertService::getAlert)
        .map(RecommendationsGeneratedUseCase::toAlertToIndex)
        .collect(toList());
  }

  private static Alert toAlertToIndex(com.silenteight.adjudication.api.v1.Alert alert) {
    return Alert.newBuilder()
        .setDiscriminator(alert.getName())
        .setPayload(toStruct(alert.getLabelsMap()))
        .build();
  }

  private static Struct toStruct(Map<String, String> payload) {
    return Struct.newBuilder()
        .putAllFields(toFields(payload))
        .build();
  }

  private static Map<String, Value> toFields(Map<String, String> payload) {
    return payload.entrySet()
        .stream()
        .collect(toMap(Entry::getKey, field -> toValue(field.getValue())));
  }

  private static Value toValue(String fieldValue) {
    return Value.newBuilder()
        .setStringValue(fieldValue)
        .build();
  }
}
