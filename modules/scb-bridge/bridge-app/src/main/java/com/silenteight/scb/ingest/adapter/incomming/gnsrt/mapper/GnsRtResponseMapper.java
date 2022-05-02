package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.scb.ingest.adapter.incomming.common.protocol.RecommendedActionUtils;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert.RecommendationEnum;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseMatch;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Match;

import java.util.List;

import static com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import static com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;

@AllArgsConstructor
public class GnsRtResponseMapper {

  private final GnsRtResponseMapperConfigurationProperties mapperConfigurationProperties;

  public GnsRtResponseAlert map(
      @NonNull GnsRtAlert gnsRtAlert,
      @NonNull Recommendation recommendation) {

    var responseAlertBuilder = GnsRtResponseAlert.builder()
        .alertId(recommendation.alert().id())
        .comments(recommendation.recommendedComment())
        .recommendation(narrowRecommendationEnum(recommendation.recommendedAction()))
        .recommendationTimestamp(recommendation.recommendedAt().toLocalDateTime())
        .watchlistType(gnsRtAlert.getWatchlistType());

    if (mapperConfigurationProperties.attachQcoFieldsToResponse()) {
      responseAlertBuilder
          .policyId(recommendation.policyId())
          .matches(getRtResponseMatches(recommendation));
    }
    return responseAlertBuilder.build();
  }

  private List<GnsRtResponseMatch> getRtResponseMatches(Recommendation recommendation) {
    return recommendation.matches()
        .stream()
        .map(GnsRtResponseMapper::gnsRtResponseMatch)
        .toList();
  }

  private static RecommendationEnum narrowRecommendationEnum(
      RecommendedAction recommendedAction) {
    switch (recommendedAction) {
      case ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE:
      case ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE:
      case ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE:
        return RecommendationEnum.INVESTIGATE;
      default:
        var actionName = RecommendedActionUtils.nameWithoutPrefix(recommendedAction);
        return RecommendationEnum.fromValue(actionName);
    }
  }

  private static GnsRtResponseMatch gnsRtResponseMatch(Match match) {
    return GnsRtResponseMatch.builder()
        .hitID(match.id())
        .fvSignature(match.fvSignature())
        .stepId(match.stepId())
        .qaSampled(match.qaSampled())
        .build();
  }
}
