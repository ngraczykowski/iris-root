package com.silenteight.bridge.core.reports.domain;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.silenteight.bridge.core.reports.domain.model.*;
import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto.MatchDto;
import com.silenteight.bridge.core.reports.domain.model.Report.AlertData;
import com.silenteight.bridge.core.reports.domain.model.Report.AlertData.AlertDataBuilder;
import com.silenteight.bridge.core.reports.domain.model.Report.MatchData;

import com.github.wnameless.json.flattener.JsonFlattener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ReportsMapper {

  private static final String EMPTY_STRING = "";
  private static final String DEFAULT_COMMENT = EMPTY_STRING;
  private static final String STEP_KEY = "step";
  private static final String POLICY_KEY = "policy";
  private static final String STEP_TITLE_KEY = "step_title";
  private static final String POLICY_TITLE_KEY = "policy_title";
  private static final String FV_SIGNATURE_KEY = "feature_vector_signature";
  private static final String SOLUTION_SUFFIX = ":solution";

  Report toReport(
      String batchId, AlertWithMatchesDto alert, RecommendationWithMetadataDto recommendation) {
    return Report.builder()
        .batchId(batchId)
        .analysisName(Optional.ofNullable(recommendation)
            .map(RecommendationWithMetadataDto::analysisName)
            .orElse(EMPTY_STRING))
        .alertData(getAlertData(alert, recommendation))
        .matches(getMatches(alert, recommendation))
        .build();
  }

  Report toErroneousReport(String batchId, AlertWithMatchesDto alert) {
    return Report.builder()
        .batchId(batchId)
        .analysisName(EMPTY_STRING)
        .alertData(getAlertData(alert))
        .matches(alert.matches().stream()
            .map(matchDto -> MatchData.builder()
                .id(matchDto.id())
                .name(matchDto.name())
                .build())
            .toList()
        )
        .build();
  }

  private AlertData getAlertData(AlertWithMatchesDto alert) {
    return getAlertData(alert, null);
  }

  private AlertData getAlertData(
      AlertWithMatchesDto alert, RecommendationWithMetadataDto recommendation) {
    var alertDataBuilder = AlertData.builder()
        .id(alert.id())
        .name(alert.name())
        .errorDescription(alert.errorDescription())
        .status(alert.status());

    Optional.ofNullable(alert.metadata())
        .filter(StringUtils::isNotBlank)
        .ifPresent(metadata -> alertDataBuilder.metadata(flatten(metadata)));

    Optional.ofNullable(recommendation)
        .ifPresent(recommendationWithMetadataDto ->
            addRecommendationData(recommendationWithMetadataDto, alertDataBuilder));

    return alertDataBuilder.build();
  }

  private void addRecommendationData(
      RecommendationWithMetadataDto recommendation, AlertDataBuilder alertDataBuilder) {
    alertDataBuilder.recommendation(recommendation.recommendedAction());
    alertDataBuilder.comment(recommendation.recommendationComment());
    alertDataBuilder.recommendedAt(recommendation.recommendedAt());

    getMatchMetadata(recommendation).stream()
        .findAny()
        .ifPresent(match -> {
          alertDataBuilder.policyId(getFromReasonWithDefault(match, POLICY_KEY));
          alertDataBuilder.policyTitle(getFromReasonWithDefault(match, POLICY_TITLE_KEY));
        });
  }

  private List<MatchData> getMatches(
      AlertWithMatchesDto alert, RecommendationWithMetadataDto recommendation) {
    var matchNameToMatchId = alert.matches().stream()
        .collect(Collectors.toMap(MatchDto::name, MatchDto::id));

    return getMatchMetadata(recommendation).stream()
        .map(match -> MatchData.builder()
            .id(matchNameToMatchId.get(match.match()))
            .name(match.match())
            .recommendation(match.solution())
            .comment(DEFAULT_COMMENT)
            .stepId(getFromReasonWithDefault(match, STEP_KEY))
            .stepTitle(getFromReasonWithDefault(match, STEP_TITLE_KEY))
            .fvSignature(getFromReasonWithDefault(match, FV_SIGNATURE_KEY))
            .features(getFeatures(match))
            .categories(match.categories())
            .build()
        ).toList();
  }

  private List<MatchMetadataDto> getMatchMetadata(RecommendationWithMetadataDto recommendation) {
    return Optional.ofNullable(recommendation)
        .map(RecommendationWithMetadataDto::metadata)
        .map(RecommendationMetadataDto::matchMetadata)
        .orElse(List.of());
  }

  private String getFromReasonWithDefault(MatchMetadataDto match, String key) {
    return match.reason().getOrDefault(key, EMPTY_STRING);
  }

  private Map<String, String> getFeatures(MatchMetadataDto match) {
    return match.features()
        .entrySet()
        .stream()
        .collect(
            Collectors.toMap(
                entry -> entry.getKey() + SOLUTION_SUFFIX, entry -> entry.getValue().solution()));
  }

  @SneakyThrows
  private Map<String, Object> flatten(String json) {
    return JsonFlattener.flattenAsMap(json);
  }
}
