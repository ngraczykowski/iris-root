package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtHit;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class GnsRtManualInvestigationRecomBuilder {

  Recommendations prepareManualInvestigationRecommendation(
      GnsRtRecommendationRequest request) {
    List<Recommendation> recommendations = getAlerts(request).stream()
        .map(GnsRtManualInvestigationRecomBuilder::createRecommendation)
        .toList();
    return Recommendations.builder().recommendations(recommendations).build();
  }

  private List<GnsRtAlert> getAlerts(GnsRtRecommendationRequest request) {
    return request
        .getScreenCustomerNameRes()
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo()
        .getImmediateResponseData()
        .getAlerts();
  }

  private Recommendation createRecommendation(GnsRtAlert gnsRtAlert) {
    return Recommendation.builder()
        .alert(Recommendations.Alert.builder()
            .id(gnsRtAlert.getAlertId())
            .build())
        .recommendedAction(RecommendedAction.ACTION_INVESTIGATE)
        .recommendedComment(prepareComment(gnsRtAlert))
        .recommendedAt(OffsetDateTime.now())
        .matches(Collections.emptyList())
        .build();
  }

  private String prepareComment(GnsRtAlert alert) {
    StringBuilder commentStringBuilder = new StringBuilder();
    commentStringBuilder.append(
        "S8 recommended action: Manual Investigation\n\nManual Investigation hits:");
    alert
        .getHitList()
        .stream()
        .map(GnsRtHit::getHitId)
        .distinct()
        .forEach(hitId -> commentStringBuilder.append("\nMatch ").append(hitId));
    return commentStringBuilder.toString();
  }
}
