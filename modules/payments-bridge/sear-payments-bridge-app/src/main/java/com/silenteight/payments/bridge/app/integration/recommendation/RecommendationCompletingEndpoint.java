package com.silenteight.payments.bridge.app.integration.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertIdUseCase;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.UUID;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class RecommendationCompletingEndpoint {

  private static final String INT_CHANNEL = "RecommendationCompletingEndpoint_int_channel";
  private static final String INT_SPLITTER_CHANNEL =
      "RecommendationCompletingEndpoint_int_splitter_channel";

  private final RecommendationClientPort recommendationClientPort;
  private final GetRegisteredAlertIdUseCase getRegisteredAlertIdUseCase;
  private final AnalysisDataAccessPort analysisDataAccessPort;

  @Filter(inputChannel = RecommendationGeneratedEvent.CHANNEL, outputChannel = INT_SPLITTER_CHANNEL)
  boolean filterAnalysis(RecommendationGeneratedEvent event) {
    var notification = event.getRecommendationsGenerated();
    return analysisDataAccessPort.existsAnalysis(notification.getAnalysis());
  }

  @Splitter(inputChannel = INT_SPLITTER_CHANNEL, outputChannel = INT_CHANNEL)
  List<RecommendationInfo> split(RecommendationGeneratedEvent event) {
    var notification = event.getRecommendationsGenerated();
    if (log.isDebugEnabled()) {
      log.debug(
          "Received recommendation generated notification: count={}",
          notification.getRecommendationInfosCount());
    }

    return notification.getRecommendationInfosList();
  }

  @ServiceActivator(inputChannel = INT_CHANNEL,
      outputChannel = RecommendationCompletedEvent.CHANNEL)
  RecommendationCompletedEvent completing(RecommendationInfo recommendationInfo) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Retrieving recommendation: recommendation={}, alert={}",
          recommendationInfo.getRecommendation(), recommendationInfo.getAlert());
    }

    var recommendationWithMetadata =
        recommendationClientPort.receiveRecommendation(
            GetRecommendationRequest.newBuilder()
                .setRecommendation(recommendationInfo.getRecommendation()).build());

    var recommendation = recommendationWithMetadata.getRecommendation();
    var alertId = getRegisteredAlertIdUseCase.getAlertId(recommendation.getAlert());

    return RecommendationCompletedEvent.fromAdjudication(
        UUID.fromString(alertId), recommendationWithMetadata);
  }

  @Bean(INT_SPLITTER_CHANNEL)
  MessageChannel intSplitterChannel() {
    return new DirectChannel();
  }

  @Bean(INT_CHANNEL)
  MessageChannel intChannel() {
    return new DirectChannel();
  }

}
