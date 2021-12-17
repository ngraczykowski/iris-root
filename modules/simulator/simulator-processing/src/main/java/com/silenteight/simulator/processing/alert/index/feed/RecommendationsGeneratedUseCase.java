package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.amqp.gateway.SimulationDataIndexRequestGateway;
import com.silenteight.simulator.processing.alert.index.amqp.listener.RecommendationsGeneratedMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
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
  @NonNull
  private final AnalysisService analysisService;
  @NonNull
  private final SimulationDataIndexRequestGateway simulationDataIndexRequestGateway;
  @NonNull
  private Integer batchSize;

  @Override
  public void handle(RecommendationsGenerated request) {
    if (!simulationExists(request.getAnalysis())) {
      log.debug("Analysis is not a simulation: analysis=" + request.getAnalysis());
      return;
    }
    if (!isAnalysisDone(request.getAnalysis())) {
      log.debug("Analysis {} is not finished yet.", request.getAnalysis());
      return;
    }

    try {
      simulationService.streaming(request.getAnalysis());
    } catch (RuntimeException e) {
      log.warn("The analysis is not in the RUNNING state. "
                   + "The other message could already trigger streaming.");
      return;
    }

    sendAsBatches(
        recommendationService.streamRecommendationsWithMetadata(request.getAnalysis()),
        request.getAnalysis());
  }

  private boolean simulationExists(String analysisName) {
    return simulationService.exists(analysisName);
  }

  private boolean isAnalysisDone(String analysis) {
    return DONE == analysisService.getAnalysis(analysis).getState();
  }

  private void sendAsBatches(
      Iterator<RecommendationWithMetadata> iterator,
      String analysisName) {

    RecommendationWithMetaDataBatch recommendationWithMetaDataBatch
        = getRecommendationWithMetaDataBatch();
    iterator.forEachRemaining(recommendationWithMetadata -> {
      if (!recommendationWithMetaDataBatch.isComplete())
        recommendationWithMetaDataBatch.addItem(recommendationWithMetadata);
      if (recommendationWithMetaDataBatch.isComplete() || !iterator.hasNext()) {
        sendToFeed(analysisName, recommendationWithMetaDataBatch.getRecommendations());
        recommendationWithMetaDataBatch.clear();
      }
    });
  }

  private void sendToFeed(
      String analysisName,
      List<RecommendationWithMetadata> recommendationsWithMetadata) {

    String requestId = requestIdGenerator.generate();
    log.debug("Recommendations generated: "
        + " requestId=" + requestId
        + " count=" + recommendationsWithMetadata.size());

    SimulationDataIndexRequest indexRequest
        = toIndexRequest(requestId, analysisName, recommendationsWithMetadata);
    indexedAlertService.saveAsSent(
        requestId, analysisName, recommendationsWithMetadata.size());
    log.debug("Sending recommendations to feed: requestId=" + requestId);

    simulationDataIndexRequestGateway.send(indexRequest);
  }

  private static SimulationDataIndexRequest toIndexRequest(
      String requestId, String analysisName,
      List<RecommendationWithMetadata> recommendationsWithMetadata) {

    return SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(analysisName)
        .addAllAlerts(toAlertsToIndex(recommendationsWithMetadata))
        .build();
  }

  private static Collection<Alert> toAlertsToIndex(
      List<RecommendationWithMetadata> recommendationsWithMetadata) {

    return recommendationsWithMetadata
        .stream()
        .map(RecommendationsGeneratedUseCase::toAlertToIndex)
        .collect(toList());
  }

  private static Alert toAlertToIndex(RecommendationWithMetadata recommendationWithMetadata) {
    String alertName = recommendationWithMetadata.getRecommendation().getAlert();

    return Alert.newBuilder()
        .setDiscriminator(alertName)
        .setName(alertName)
        .setPayload(toStruct(recommendationWithMetadata))
        .build();
  }

  private static Struct toStruct(RecommendationWithMetadata recommendationWithMetadata) {
    return Struct.newBuilder()
        .putAllFields(toFields(recommendationWithMetadata))
        .build();
  }

  private static Map<String, Value> toFields(
      RecommendationWithMetadata recommendationWithMetadata) {

    Recommendation recommendation = recommendationWithMetadata.getRecommendation();
    Map<String, Value> fields = new HashMap<>();
    fields.put(RECOMMENDATION_ALERT_FIELD, toValue(recommendation.getAlert()));
    fields.put(RECOMMENDATION_CREATE_TIME_FIELD,
               toValue(toIsoString(recommendation.getCreateTime())));
    fields.put(RECOMMENDATION_RECOMMENDED_ACTION_FIELD,
        toValue(recommendation.getRecommendedAction()));
    fields.put(RECOMMENDATION_COMMENT_FIELD, toValue(recommendation.getRecommendationComment()));

    recommendationWithMetadata
        .getMetadata()
        .getMatchesList()
        .stream()
        .findFirst()
        .ifPresent(matchMetadata -> fields.putAll(toMatchFields(matchMetadata)));

    return fields;
  }

  @NotNull
  private static String toIsoString(Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()).toString();
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
    fields.put(key + FEATURE_REASON_FIELD_POSTFIX,
        toValue(featureMetadata.getReason().toString()));

    return fields;
  }

  private static Value toValue(String fieldValue) {
    return Value.newBuilder()
        .setStringValue(fieldValue)
        .build();
  }

  private RecommendationWithMetaDataBatch getRecommendationWithMetaDataBatch() {
    return new RecommendationWithMetaDataBatch(batchSize);
  }
}
