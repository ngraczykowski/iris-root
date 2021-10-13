package com.silenteight.payments.bridge.mock.ae;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;

import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.UUID;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_INPUT_ACCEPTED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_GENERATED;

@Profile("mockae")
@MessageEndpoint
class RecommendationShortPathIntegration {

  /*
   * Simulate issuing recommendation just after universal-data-source confirmed input values.
   * This short-path does not exist in reality.
   */
  @ServiceActivator(inputChannel = ALERT_INPUT_ACCEPTED, outputChannel = RECOMMENDATION_GENERATED)
  RecommendationGeneratedEvent generateRecommendation(AlertInputAcceptedEvent event) {
    var recommendationsGenerated = RecommendationsGenerated.newBuilder()
        .addRecommendationInfos(
            RecommendationInfo.newBuilder()
                .setAlert(event.getAlertId().toString())
                .setRecommendation("analysis/MOCKAE/recommendations/" + UUID.randomUUID())
                .build()).build();

    return new RecommendationGeneratedEvent(recommendationsGenerated);
  }

}
