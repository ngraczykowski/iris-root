package com.silenteight.bridge.core.recommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics;
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics.RecommendationsStats;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto.MatchDto;
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.proto.recommendation.api.v1.*;
import com.silenteight.proto.recommendation.api.v1.Alert.AlertStatus;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus.ERROR;
import static com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus.RECOMMENDED;

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
      BatchWithAlertsDto batchWithAlerts,
      List<RecommendationWithMetadata> recommendations,
      OffsetDateTime recommendedAtForErrorAlerts,
      BatchStatistics batchStatistics) {
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
        .setStatistics(toStatistics(batchStatistics))
        .build();
  }

  private Recommendation.Builder toRecommendationBuilder(
      AlertWithMatchesDto alert,
      RecommendationWithMetadata recommendationWithMetadata,
      OffsetDateTime recommendedAtForErrorAlerts) {

    return Optional.ofNullable(recommendationWithMetadata)
        .map(recommendation -> createSuccessfulRecommendation(alert, recommendation))
        .orElseGet(() -> createErrorRecommendation(alert, recommendedAtForErrorAlerts));
  }

  private Recommendation.Builder createErrorRecommendation(
      AlertWithMatchesDto alert, OffsetDateTime recommendedAtForErrorAlerts) {
    return Recommendation.newBuilder()
        .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(recommendedAtForErrorAlerts))
        .setAlert(getProtoAlert(alert));
  }

  private Recommendation.Builder createSuccessfulRecommendation(
      AlertWithMatchesDto alert, RecommendationWithMetadata recommendation) {
    return Recommendation.newBuilder()
        .setName(recommendation.name())
        .setRecommendedAction(recommendation.recommendedAction())
        .setRecommendationComment(recommendation.recommendationComment())
        .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(recommendation.recommendedAt()))
        .setAlert(getProtoAlert(alert))
        .addAllMatches(getProtoMatches(recommendation, alert));
  }

  private Alert getProtoAlert(AlertWithMatchesDto alert) {
    return Alert.newBuilder()
        .setId(alert.id())
        .setStatus(toStatus(alert))
        .setMetadata(Optional.ofNullable(alert.metadata()).orElse(EMPTY_STRING))
        .setErrorMessage(Optional.ofNullable(alert.errorDescription()).orElse(EMPTY_STRING))
        .build();
  }

  private AlertStatus toStatus(AlertWithMatchesDto alert) {
    if (RECOMMENDED == alert.status()) {
      return AlertStatus.SUCCESS;
    } else if (ERROR == alert.status()) {
      return AlertStatus.FAILURE;
    } else {
      throw new IllegalStateException("Alert status should be ERROR/RECOMMENDED at this point");
    }
  }

  private List<Match> getProtoMatches(
      RecommendationWithMetadata recommendation, AlertWithMatchesDto matchingAlert) {
    return recommendation.metadata().matchMetadata().stream()
        .map(matchMetadata -> toProtoMatch(matchMetadata, matchingAlert))
        .toList();
  }

  private Match toProtoMatch(MatchMetadata matchMetadata, AlertWithMatchesDto alert) {
    return Match.newBuilder()
        .setId(alert.matches().stream()
            .filter(match -> match.name().equals(matchMetadata.match()))
            .map(MatchDto::id)
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
    return builder.build();
  }

  private Statistics toStatistics(BatchStatistics batchStatistics) {
    return Statistics.newBuilder()
        .setTotalProcessedCount(batchStatistics.totalProcessedCount())
        .setRecommendedAlertsCount(batchStatistics.recommendedAlertsCount())
        .setTotalUnableToProcessCount(batchStatistics.totalUnableToProcessCount())
        .setRecommendationsStatistics(
            toRecommendationsStatistics(batchStatistics.recommendationsStats()))
        .build();
  }

  private RecommendationsStatistics toRecommendationsStatistics(RecommendationsStats stats) {
    return RecommendationsStatistics.newBuilder()
        .setTruePositiveCount(stats.truePositiveCount())
        .setFalsePositiveCount(stats.falsePositiveCount())
        .setManualInvestigationCount(stats.manualInvestigationCount())
        .setErrorCount(stats.errorCount())
        .build();
  }
}