package com.silenteight.bridge.core.recommendation.adapter.incoming;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.registration.domain.model.Alert.Status;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;
import com.silenteight.proto.recommendation.api.v1.Alert;
import com.silenteight.proto.recommendation.api.v1.Alert.AlertStatus;
import com.silenteight.proto.recommendation.api.v1.Match;
import com.silenteight.proto.recommendation.api.v1.Recommendation;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
class RecommendationMapper {

  private static final String EMPTY_STRING = "";
  private static final String DEFAULT_STEP = EMPTY_STRING;
  private static final String DEFAULT_SIGNATURE = EMPTY_STRING;
  private static final String DEFAULT_COMMENT = EMPTY_STRING;
  private static final String STEP_KEY = "step";
  private static final String FV_SIGNATURE_KEY = "feature_vector_signature";
  private static final String FEATURE_PREFIX = "features/";

  RecommendationsResponse toRecommendationsResponse(
      BatchWithAlerts batchWithAlerts, List<RecommendationWithMetadata> recommendations,
      OffsetDateTime recommendedAtForErrorAlerts) {
    var alertToRecommendation = recommendations.stream()
        .collect(Collectors.toMap(RecommendationWithMetadata::alertName, Function.identity()));

    return RecommendationsResponse.newBuilder()
        .addAllRecommendations(batchWithAlerts.alerts().stream()
            .map(alert -> toRecommendationBuilder(alert, alertToRecommendation.get(alert.name()),
                recommendedAtForErrorAlerts))
            .map(builder -> builder
                .setBatchId(batchWithAlerts.batchId())
                .setPolicyId(batchWithAlerts.policyId())
                .build())
            .toList())
        .build();
  }

  private Recommendation.Builder toRecommendationBuilder(
      AlertWithMatches alert, RecommendationWithMetadata recommendationWithMetadata,
      OffsetDateTime recommendedAtForErrorAlerts) {

    return Optional.ofNullable(recommendationWithMetadata)
        .map(recommendation -> createSuccessfulRecommendation(alert, recommendation))
        .orElseGet(() -> createErrorRecommendation(alert, recommendedAtForErrorAlerts));
  }

  private Recommendation.Builder createErrorRecommendation(
      AlertWithMatches alert, OffsetDateTime recommendedAtForErrorAlerts) {
    return Recommendation.newBuilder()
        .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(recommendedAtForErrorAlerts))
        .setAlert(getProtoAlert(alert));
  }

  private Recommendation.Builder createSuccessfulRecommendation(
      AlertWithMatches alert, RecommendationWithMetadata recommendation) {
    return Recommendation.newBuilder()
        .setName(recommendation.name())
        .setRecommendedAction(recommendation.recommendedAction())
        .setRecommendationComment(recommendation.recommendationComment())
        .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(recommendation.recommendedAt()))
        .setAlert(getProtoAlert(alert))
        .addAllMatches(getProtoMatches(recommendation, alert));
  }

  private Alert getProtoAlert(AlertWithMatches alert) {
    return Alert.newBuilder()
        .setId(alert.id())
        .setStatus(toStatus(alert))
        .setMetadata(Optional.ofNullable(alert.metadata()).orElse(EMPTY_STRING))
        .setErrorMessage(Optional.ofNullable(alert.errorDescription()).orElse(EMPTY_STRING))
        .build();
  }

  private AlertStatus toStatus(AlertWithMatches alert) {
    if (Status.RECOMMENDED == alert.status()) {
      return AlertStatus.SUCCESS;
    } else if (Status.ERROR == alert.status()) {
      return AlertStatus.FAILURE;
    }
    throw new IllegalStateException("Alert status should be ERROR/RECOMMENDED at this point");
  }

  private List<Match> getProtoMatches(
      RecommendationWithMetadata recommendation, AlertWithMatches matchingAlert) {
    return recommendation.metadata().matchMetadata().stream()
        .map(matchMetadata -> toProtoMatch(matchMetadata, matchingAlert))
        .toList();
  }

  private Match toProtoMatch(MatchMetadata matchMetadata, AlertWithMatches alert) {
    return Match.newBuilder()
        .setId(alert.matches().stream()
            .filter(match -> match.name().equals(matchMetadata.match()))
            .map(AlertWithMatches.Match::id)
            .findAny()
            .orElseThrow()
        )
        .setRecommendedAction(matchMetadata.solution())
        .setRecommendationComment(DEFAULT_COMMENT) //TODO verify if it is needed
        .setStepId(matchMetadata.reason().getOrDefault(STEP_KEY, DEFAULT_STEP))
        .setFvSignature(
            matchMetadata.reason().getOrDefault(FV_SIGNATURE_KEY, DEFAULT_SIGNATURE))
        .setFeatures(getFeatures(matchMetadata))
        .build();
  }

  private Struct getFeatures(MatchMetadata matchMetadata) {
    Builder builder = Struct.newBuilder();
    matchMetadata.features().forEach((key, value) ->
        builder.putFields(
            key.replace(FEATURE_PREFIX, ""),
            Value.newBuilder().setStringValue(value.solution()).build()));
    return builder
        .build();
  }
}
