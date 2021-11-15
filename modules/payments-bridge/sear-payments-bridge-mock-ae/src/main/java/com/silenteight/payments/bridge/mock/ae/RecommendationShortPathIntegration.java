package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;

import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ThreadLocalRandom;

@Profile("mockae")
@MessageEndpoint
@RequiredArgsConstructor
class RecommendationShortPathIntegration {

  private final JdbcTemplate jdbcTemplate;

  /*
   * Simulate issuing recommendation just after universal-data-source has accepted input values.
   */
  private String getAnalysisName() {
    return jdbcTemplate.queryForObject("select analysis_name from pb_analysis "
        + "where created_at > now() - interval '15 min' order by created_at desc limit 1",
        String.class);
  }

  @ServiceActivator(inputChannel = AlertInputAcceptedEvent.CHANNEL,
      outputChannel = RecommendationGeneratedEvent.CHANNEL)
  RecommendationGeneratedEvent generateRecommendation(AlertInputAcceptedEvent event) {
    var analysisName = getAnalysisName();
    var recommendationId = ThreadLocalRandom.current().nextInt(10000) + 1;
    var recommendationsGenerated = RecommendationsGenerated.newBuilder()
        .setAnalysis(analysisName)
        .addRecommendationInfos(
            RecommendationInfo.newBuilder()
                .setAlert(event.getAeAlert().getAlertName())
                .setRecommendation("analysis/MOCKAE/recommendations/" + recommendationId)
                .build()).build();

    return new RecommendationGeneratedEvent(recommendationsGenerated);
  }

}
