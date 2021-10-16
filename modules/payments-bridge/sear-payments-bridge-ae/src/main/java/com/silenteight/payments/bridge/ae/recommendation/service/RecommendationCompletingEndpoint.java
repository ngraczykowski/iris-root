package com.silenteight.payments.bridge.ae.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertIdUseCase;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.UUID;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_GENERATED;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class RecommendationCompletingEndpoint {

  private static final String INT_CHANNEL = "RecommendationCompletingEndpoint_int_channel";

  private final RecommendationClientPort recommendationClientPort;
  private final GetRegisteredAlertIdUseCase getRegisteredAlertIdUseCase;

  @Splitter(inputChannel = RECOMMENDATION_GENERATED, outputChannel = INT_CHANNEL)
  List<RecommendationInfo> split(RecommendationGeneratedEvent event) {
    return event.getRecommendationsGenerated().getRecommendationInfosList();
  }

  @ServiceActivator(inputChannel = INT_CHANNEL, outputChannel = RECOMMENDATION_COMPLETED)
  RecommendationCompletedEvent completing(RecommendationInfo recommendationInfo) {
    var recommendationWithMetadata =
        recommendationClientPort.receiveRecommendation(
            GetRecommendationRequest.newBuilder()
                .setRecommendation(recommendationInfo.getRecommendation()).build());

    var recommendation = recommendationWithMetadata.getRecommendation();
    var alertId = getRegisteredAlertIdUseCase.getAlertId(recommendation.getAlert());

    return new RecommendationCompletedEvent(recommendationWithMetadata, UUID.fromString(alertId));
  }

  @Bean(INT_CHANNEL)
  MessageChannel intChannel() {
    return new DirectChannel();
  }

}
