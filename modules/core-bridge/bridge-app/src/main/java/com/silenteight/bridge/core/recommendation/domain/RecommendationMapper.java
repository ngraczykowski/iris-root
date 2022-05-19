package com.silenteight.bridge.core.recommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.bridge.core.recommendation.domain.model.*;
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics.RecommendationsStats;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto.MatchDto;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.proto.recommendation.api.v1.*;
import com.silenteight.proto.recommendation.api.v1.Alert.AlertStatus;
import com.silenteight.proto.recommendation.api.v1.RecommendationsStatistics;

import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus.DELIVERED;
import static com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus.ERROR;
import static com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus.RECOMMENDED;

@UtilityClass
class RecommendationMapper {

  private static final String EMPTY_STRING = "";
  private static final String DEFAULT_STEP = EMPTY_STRING;
  private static final String DEFAULT_SIGNATURE = EMPTY_STRING;
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

  RecommendationResponse toRecommendationResponse(RecommendationDto recommendation) {
    return RecommendationResponse.newBuilder()
        .setRecommendation(
            toRecommendationBuilder(
                recommendation.alert(),
                recommendation.matchWithAlertIds(),
                recommendation.recommendation(),
                recommendation.recommendedAtForErrorAlerts())
                .setBatchId(recommendation.batchId().id())
                .setPolicyId(recommendation.batchId().policyId())
                .build())
        .setStatistics(toStatistics(recommendation.statistics()))
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

  private Recommendation.Builder toRecommendationBuilder(
      AlertWithoutMatches alert,
      List<MatchWithAlertId> matchWithAlertIds,
      RecommendationWithMetadata recommendationWithMetadata,
      OffsetDateTime recommendedAtForErrorAlerts) {
    return Optional.ofNullable(recommendationWithMetadata)
        .map(recommendation -> createSuccessfulRecommendation(
            alert,
            matchWithAlertIds,
            recommendation))
        .orElseGet(() -> createErrorRecommendation(alert, recommendedAtForErrorAlerts));
  }

  private Recommendation.Builder createErrorRecommendation(
      AlertWithMatchesDto alert, OffsetDateTime recommendedAtForErrorAlerts) {
    return Recommendation.newBuilder()
        .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(recommendedAtForErrorAlerts))
        .setAlert(getProtoAlert(alert));
  }

  private static Recommendation.Builder createErrorRecommendation(
      AlertWithoutMatches alert,
      OffsetDateTime recommendedAtForErrorAlerts) {
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

  private static Recommendation.Builder createSuccessfulRecommendation(
      AlertWithoutMatches alert,
      List<MatchWithAlertId> matchWithAlertIds,
      RecommendationWithMetadata recommendation) {
    return Recommendation.newBuilder()
        .setName(recommendation.name())
        .setRecommendedAction(recommendation.recommendedAction())
        .setRecommendationComment(recommendation.recommendationComment())
        .setRecommendedAt(TimeStampUtil.fromOffsetDateTime(recommendation.recommendedAt()))
        .setAlert(getProtoAlert(alert))
        .addAllMatches(getProtoMatches(recommendation, matchWithAlertIds));
  }

  private Alert getProtoAlert(AlertWithMatchesDto alert) {
    return Alert.newBuilder()
        .setId(alert.id())
        .setStatus(toStatus(alert))
        .setMetadata(Optional.ofNullable(alert.metadata()).orElse(EMPTY_STRING))
        .setErrorMessage(Optional.ofNullable(alert.errorDescription()).orElse(EMPTY_STRING))
        .setName(Optional.ofNullable(alert.name()).orElse(EMPTY_STRING))
        .build();
  }

  private static Alert getProtoAlert(AlertWithoutMatches alert) {
    return Alert.newBuilder()
        .setId(alert.alertId())
        .setStatus(toStatus(alert))
        .setMetadata(Optional.ofNullable(alert.metadata()).orElse(EMPTY_STRING))
        .setErrorMessage(Optional.ofNullable(alert.errorDescription()).orElse(EMPTY_STRING))
        .setName(Optional.ofNullable(alert.alertName()).orElse(EMPTY_STRING))
        .build();
  }

  private AlertStatus toStatus(AlertWithMatchesDto alert) {
    if (RECOMMENDED == alert.status() || DELIVERED == alert.status()) {
      return AlertStatus.SUCCESS;
    } else if (ERROR == alert.status()) {
      return AlertStatus.FAILURE;
    } else {
      throw new IllegalStateException(
          "Alert status should be ERROR/RECOMMENDED/DELIVERED at this point");
    }
  }

  private static AlertStatus toStatus(AlertWithoutMatches alert) {
    if (AlertWithoutMatches.AlertStatus.RECOMMENDED == alert.alertStatus()
        || AlertWithoutMatches.AlertStatus.DELIVERED == alert.alertStatus()) {
      return AlertStatus.SUCCESS;
    } else if (AlertWithoutMatches.AlertStatus.ERROR == alert.alertStatus()) {
      return AlertStatus.FAILURE;
    } else {
      throw new IllegalStateException(
          "Alert status should be ERROR/RECOMMENDED/DELIVERED at this point");
    }
  }

  private List<Match> getProtoMatches(
      RecommendationWithMetadata recommendation, AlertWithMatchesDto matchingAlert) {
    return Optional.ofNullable(recommendation.metadata())
        .map(metadata -> metadata.matchMetadata().stream()
            .map(matchMetadata -> toProtoMatch(matchMetadata, matchingAlert))
            .toList())
        .orElseGet(List::of);
  }

  private static List<Match> getProtoMatches(
      RecommendationWithMetadata recommendation, List<MatchWithAlertId> matchWithAlertIds) {
    return Optional.ofNullable(recommendation.metadata())
        .map(metadata -> metadata.matchMetadata()
            .stream()
            .map(matchMetadata -> toProtoMatch(matchMetadata, matchWithAlertIds))
            .toList())
        .orElseGet(List::of);
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
        .setRecommendationComment(
            Optional.ofNullable(matchMetadata.matchComment()).orElse(EMPTY_STRING))
        .setStepId(matchMetadata.reason().getOrDefault(STEP_KEY, DEFAULT_STEP))
        .setFvSignature(
            matchMetadata.reason().getOrDefault(FV_SIGNATURE_KEY, DEFAULT_SIGNATURE))
        .setFeatures(getFeatures(matchMetadata))
        .setName(Optional.ofNullable(matchMetadata.match()).orElse(EMPTY_STRING))
        .build();
  }

  private static Match toProtoMatch(
      MatchMetadata matchMetadata, List<MatchWithAlertId> matchWithAlertIds) {
    return Match.newBuilder()
        .setId(matchWithAlertIds.stream()
            .filter(match -> match.name().equals(matchMetadata.match()))
            .map(MatchWithAlertId::id)
            .findAny()
            .orElseThrow())
        .setRecommendedAction(matchMetadata.solution())
        .setRecommendationComment(
            Optional.ofNullable(matchMetadata.matchComment()).orElse(EMPTY_STRING))
        .setStepId(matchMetadata.reason()
            .getOrDefault(STEP_KEY, DEFAULT_STEP))
        .setFvSignature(matchMetadata.reason()
            .getOrDefault(FV_SIGNATURE_KEY, DEFAULT_SIGNATURE))
        .setFeatures(getFeatures(matchMetadata))
        .setName(Optional.ofNullable(matchMetadata.match()).orElse(EMPTY_STRING))
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
