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
import com.silenteight.data.api.v2.SimulationAlert;
import com.silenteight.data.api.v2.SimulationDataIndexRequest;
import com.silenteight.data.api.v2.SimulationMatch;
import com.silenteight.simulator.processing.alert.index.amqp.gateway.SimulationDataIndexRequestGateway;
import com.silenteight.simulator.processing.alert.index.amqp.listener.RecommendationsGeneratedMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.time.Instant.ofEpochSecond;
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
  private static final String MATCH_COMMENT_FIELD = "match_comment";
  private static final String FEATURE_CONFIG_FIELD_POSTFIX = ":config";
  private static final String FEATURE_SOLUTION_FIELD_POSTFIX = ":solution";
  private static final String FEATURE_REASON_FIELD_POSTFIX = ":reason";
  private static final String CATEGORY_FIELD_POSTFIX = ":value";

  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final RequestIdGenerator requestIdGenerator;
  @NonNull
  private final SimulationDataIndexRequestGateway simulationDataIndexRequestGateway;
  @NonNull
  private Integer batchSize;

  @Override
  public void handle(RecommendationsGenerated request) {
    log.info("RecommendationsGenerated request received, analysisName={}", request.getAnalysis());
    List<RecommendationInfo> recommendationInfosList =
        request.getRecommendationInfosList();

    sendAsBatches(recommendationInfosList.iterator(), request.getAnalysis());
    log.debug("RecommendationsGenerated request processed, analysisName={}", request.getAnalysis());
  }

  private void sendAsBatches(
      Iterator<RecommendationInfo> iterator, String analysisName) {

    RecommendationInfoBatch recommendationInfoBatch = getRecommendationWithMetaDataBatch();

    while (iterator.hasNext()) {
      RecommendationInfo recommendationInfo = iterator.next();
      if (!recommendationInfoBatch.isComplete())
        recommendationInfoBatch.addItem(recommendationInfo);
      if (recommendationInfoBatch.isComplete() || !iterator.hasNext()) {
        sendToFeed(analysisName, recommendationInfoBatch.getRecommendations());
        recommendationInfoBatch.clear();
      }
    }
  }

  private void sendToFeed(String analysisName, List<RecommendationInfo> recommendationInfoList) {
    String requestId = requestIdGenerator.generate();

    log.info("Recommendations generated: analysisName={} requestId={} count={}",
        analysisName, requestId, recommendationInfoList.size());

    SimulationDataIndexRequest indexRequest
        = toIndexRequest(requestId, analysisName, recommendationInfoList);

    indexedAlertService.saveAsSent(requestId, analysisName, recommendationInfoList.size());

    log.debug("Sending recommendations to feed: requestId={}.", requestId);
    simulationDataIndexRequestGateway.send(indexRequest);
  }

  private static SimulationDataIndexRequest toIndexRequest(
      String requestId, String analysisName,
      List<RecommendationInfo> recommendationInfo) {

    return SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(analysisName)
        .addAllAlerts(toAlertsToIndex(recommendationInfo))
        .build();
  }

  private static List<SimulationAlert> toAlertsToIndex(
      List<RecommendationInfo> recommendationsWithMetadata) {

    return recommendationsWithMetadata
        .stream()
        .map(RecommendationsGeneratedUseCase::toAlertToIndex)
        .collect(toList());
  }

  private static SimulationAlert toAlertToIndex(RecommendationInfo recommendationInfo) {
    String alertName = recommendationInfo.getAlert();
    return SimulationAlert.newBuilder()
        .setName(alertName)
        .setPayload(toAlertStruct(recommendationInfo))
        .addAllMatches(toMatchesToIndex(recommendationInfo.getMetadata()))
        .build();
  }

  private static Struct toAlertStruct(RecommendationInfo recommendationInfo) {
    return Struct.newBuilder()
        .putAllFields(toAlertField(recommendationInfo))
        .build();
  }

  private static List<SimulationMatch> toMatchesToIndex(
      RecommendationMetadata recommendationMetadata) {

    return recommendationMetadata.getMatchesList().stream()
        .map(RecommendationsGeneratedUseCase::toMatchToIndex)
        .collect(toList());
  }

  private static SimulationMatch toMatchToIndex(MatchMetadata matchMetadata) {
    return SimulationMatch.newBuilder()
        .setName(matchMetadata.getMatch())
        .setPayload(toMatchStruct(matchMetadata))
        .build();
  }

  private static Struct toMatchStruct(MatchMetadata matchMetadata) {
    return Struct.newBuilder()
        .putAllFields(toMatchFields(matchMetadata))
        .build();
  }

  private static Map<String, Value> toAlertField(RecommendationInfo recommendationInfo) {
    Recommendation recommendation = recommendationInfo.getValue();
    Map<String, Value> fields = new HashMap<>();
    fields.put(RECOMMENDATION_ALERT_FIELD, toValue(recommendation.getAlert()));
    fields.put(
        RECOMMENDATION_CREATE_TIME_FIELD,
        toValue(toIsoString(recommendation.getCreateTime())));

    fields.put(
        RECOMMENDATION_RECOMMENDED_ACTION_FIELD,
        toValue(recommendation.getRecommendedAction()));

    fields.put(RECOMMENDATION_COMMENT_FIELD, toValue(recommendation.getRecommendationComment()));
    return fields;
  }

  @NotNull
  private static String toIsoString(Timestamp timestamp) {
    return ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()).toString();
  }

  private static Map<String, Value> toMatchFields(MatchMetadata matchMetadata) {
    Map<String, Value> fields = new HashMap<>();
    fields.put(MATCH_SOLUTION_FIELD, toValue(matchMetadata.getSolution()));
    fields.put(MATCH_COMMENT_FIELD, toValue(matchMetadata.getMatchComment()));
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
    fields.put(
        key + FEATURE_REASON_FIELD_POSTFIX,
        toValue(featureMetadata.getReason().toString()));

    return fields;
  }

  private static Value toValue(String fieldValue) {
    return Value.newBuilder()
        .setStringValue(fieldValue)
        .build();
  }

  private RecommendationInfoBatch getRecommendationWithMetaDataBatch() {
    return new RecommendationInfoBatch(batchSize);
  }
}
