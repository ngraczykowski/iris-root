package com.silenteight.customerbridge.gnsrt.mapper;

import lombok.NonNull;

import com.silenteight.customerbridge.common.protocol.RecommendedActionUtils;
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtAlert;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtResponseAlerts;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtResponseAlerts.RecommendationEnum;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction;

import java.time.LocalDateTime;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;

public class GnsRtResponseMapper {

  public GnsRtResponseAlerts map(
      @NonNull GnsRtAlert alert,
      @NonNull AlertRecommendation alertRecommendation) {

    Recommendation recommendation = alertRecommendation.getRecommendation();
    String alertId = alertRecommendation.getAlertId().getSourceId();

    GnsRtResponseAlerts responseAlert = new GnsRtResponseAlerts();
    responseAlert.setAlertId(alertId);
    responseAlert.setComments(recommendation.getComment());
    responseAlert.setRecommendationTimestamp(getRecommendationTimestamp(recommendation));
    responseAlert.setRecommendation(map(recommendation.getAction()));
    responseAlert.setWatchlistType(alert.getWatchlistType());
    return responseAlert;
  }

  private static LocalDateTime getRecommendationTimestamp(Recommendation recommendation) {
    return toOffsetDateTime(recommendation.getCreatedAt()).toLocalDateTime();
  }

  private static RecommendationEnum map(RecommendedAction recommendedAction) {
    return narrowRecommendationEnum(recommendedAction);
  }

  private static RecommendationEnum narrowRecommendationEnum(
      RecommendedAction recommendedAction) {
    switch (recommendedAction) {
      case ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE:
      case ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE:
      case ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE:
        return RecommendationEnum.INVESTIGATE;
      default:
        String actionName = RecommendedActionUtils.nameWithoutPrefix(recommendedAction);
        return RecommendationEnum.fromValue(actionName);
    }
  }
}
