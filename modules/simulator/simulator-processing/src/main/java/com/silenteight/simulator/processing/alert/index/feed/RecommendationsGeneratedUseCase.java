package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.amqp.listener.RecommendationsGeneratedMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class RecommendationsGeneratedUseCase implements RecommendationsGeneratedMessageHandler {

  private static final String RECOMMENDATION_ALERT_FIELD = "recommendation_alert";
  private static final String RECOMMENDATION_CREATE_TIME_FIELD = "recommendation_create_time";
  private static final String RECOMMENDATION_RECOMMENDED_ACTION_FIELD
      = "recommendation_recommended_action";
  private static final String RECOMMENDATION_COMMENT_FIELD = "recommendation_comment";
  private static final String MATCH_SOLUTION_FIELD = "match_solution";
  private static final String MATCH_REASON_FIELD = "match_reason";
  private static final String FEATURE_CONFIG_FIELD_POSTFIX = ":config";
  private static final String FEATURE_SOLUTION_FIELD_POSTFIX = ":solution";
  private static final String FEATURE_REASON_FIELD_POSTFIX = ":reason";
  private static final String CATEGORY_FIELD_POSTFIX = ":value";

  @NonNull
  private final SimulationService simulationService;
  @NonNull
  private final RecommendationService recommendationService;
  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final RequestIdGenerator requestIdGenerator;

  @Override
  public SimulationDataIndexRequest handle(RecommendationsGenerated request) {
    if (!simulationExists(request.getAnalysis())) {
      log.debug("Analysis is not a simulation: analysis=" + request.getAnalysis());
      return null;
    }

    String requestId = requestIdGenerator.generate();
    log.debug("Recommendations generated: "
        + " requestId=" + requestId
        + " count=" + request.getRecommendationInfosCount());

    SimulationDataIndexRequest indexRequest = toIndexRequest(requestId, request);
    indexedAlertService.saveAsSent(
        requestId, request.getAnalysis(), request.getRecommendationInfosCount());
    log.debug("Sending recommendations to feed: requestId=" + requestId);

    return indexRequest;
  }

  private boolean simulationExists(String analysisName) {
    return simulationService.exists(analysisName);
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
        .map(this::toAlertToIndex)
        .collect(toList());
  }

  private Alert toAlertToIndex(RecommendationInfo recommendationInfo) {
    Recommendation recommendation =
        recommendationService.getRecommendation(recommendationInfo.getRecommendation());
    RecommendationMetadata metadata =
        recommendationService.getMetadata(recommendationInfo.getRecommendation());

    return Alert.newBuilder()
        .setDiscriminator(recommendationInfo.getAlert())
        .setPayload(toStruct(recommendation, metadata))
        .build();
  }

  private static Struct toStruct(Recommendation recommendation, RecommendationMetadata metadata) {
    return Struct.newBuilder()
        .putAllFields(toFields(recommendation, metadata))
        .build();
  }

  private static Map<String, Value> toFields(
      Recommendation recommendation, RecommendationMetadata metadata) {

    Map<String, Value> fields = new HashMap<>();
    fields.put(RECOMMENDATION_ALERT_FIELD, toValue(recommendation.getAlert()));
    fields.put(RECOMMENDATION_CREATE_TIME_FIELD,
        toValue(recommendation.getCreateTime().toString()));
    fields.put(RECOMMENDATION_RECOMMENDED_ACTION_FIELD,
        toValue(recommendation.getRecommendedAction()));
    fields.put(RECOMMENDATION_COMMENT_FIELD, toValue(recommendation.getRecommendationComment()));

    metadata
        .getMatchesList()
        .stream()
        .findFirst()
        .ifPresent(matchMetadata -> fields.putAll(toMatchFields(matchMetadata)));

    return fields;
  }

  private static Map<String, Value> toMatchFields(MatchMetadata matchMetadata) {
    Map<String, Value> fields = new HashMap<>();
    fields.put(MATCH_SOLUTION_FIELD, toValue(matchMetadata.getSolution()));
    matchMetadata
        .getReason()
        .getFieldsMap()
        .forEach((key, value) -> fields.put(MATCH_REASON_FIELD + ":" + key, value));
    matchMetadata
        .getFeaturesMap()
        .forEach((key, value) -> fields.putAll(toFeatureFields(key, value)));
    matchMetadata
        .getCategoriesMap()
        .forEach((key, value) -> fields.put(key + CATEGORY_FIELD_POSTFIX, toValue(value)));

    return fields;
  }

  private static Map<String, Value> toFeatureFields(String key, FeatureMetadata featureMetadata) {
    Map<String, Value> fields = new HashMap<>();
    fields.put(key + FEATURE_CONFIG_FIELD_POSTFIX, toValue(featureMetadata.getAgentConfig()));
    fields.put(key + FEATURE_SOLUTION_FIELD_POSTFIX, toValue(featureMetadata.getSolution()));
    fields.put(key + FEATURE_REASON_FIELD_POSTFIX, toValue(featureMetadata.getReason().toString()));

    return fields;
  }

  private static Value toValue(String fieldValue) {
    return Value.newBuilder()
        .setStringValue(fieldValue)
        .build();
  }
}
