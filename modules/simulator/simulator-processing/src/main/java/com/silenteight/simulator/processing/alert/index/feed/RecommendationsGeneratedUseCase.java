package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
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

import static java.util.Map.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class RecommendationsGeneratedUseCase implements RecommendationsGeneratedMessageHandler {

  private static final String RECOMMENDATION_NAME_FIELD = "recommendation_name";
  private static final String RECOMMENDATION_ALERT_FIELD = "recommendation_alert";
  private static final String RECOMMENDATION_CREATE_TIME_FIELD = "recommendation_create_time";
  private static final String RECOMMENDATION_RECOMMENDED_ACTION_FIELD
      = "recommendation_recommended_action";
  private static final String RECOMMENDATION_COMMENT_FIELD = "recommendation_comment";

  @NonNull
  private final RecommendationService recommendationService;
  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final RequestIdGenerator requestIdGenerator;

  @Override
  public SimulationDataIndexRequest handle(RecommendationsGenerated request) {
    String requestId = requestIdGenerator.generate();
    log.info("Recommendations generated: "
        + " requestId=" + requestId
        + " count=" + request.getRecommendationInfosCount());

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
        .map(RecommendationInfo::getRecommendation)
        .map(recommendationService::getRecommendation)
        .map(RecommendationsGeneratedUseCase::toAlertToIndex)
        .collect(toList());
  }

  private static Alert toAlertToIndex(Recommendation recommendation) {
    return Alert.newBuilder()
        .setDiscriminator(recommendation.getAlert())
        .setPayload(toStruct(recommendation))
        .build();
  }

  private static Struct toStruct(Recommendation recommendation) {
    return Struct.newBuilder()
        .putAllFields(toFields(recommendation))
        .build();
  }

  private static Map<String, Value> toFields(Recommendation recommendation) {
    return of(
        RECOMMENDATION_NAME_FIELD, toValue(recommendation.getName()),
        RECOMMENDATION_ALERT_FIELD, toValue(recommendation.getAlert()),
        RECOMMENDATION_CREATE_TIME_FIELD, toValue(recommendation.getCreateTime().toString()),
        RECOMMENDATION_RECOMMENDED_ACTION_FIELD, toValue(recommendation.getRecommendedAction()),
        RECOMMENDATION_COMMENT_FIELD, toValue(recommendation.getRecommendationComment()));
  }

  private static Value toValue(String fieldValue) {
    return Value.newBuilder()
        .setStringValue(fieldValue)
        .build();
  }
}
