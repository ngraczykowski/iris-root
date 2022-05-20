package com.silenteight.adjudication.engine.solving.infrastructure.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
class RecommendationPublisherImpl implements RecommendationPublisher {

  private static final String RECOMMENDATION_NOTIFICATION_EXCHANGE_NAME = "ae.event";
  private static final String RECOMMENDATIONS_GENERATED_ROUTING_KEY =
      "ae.event.recommendations-generated";
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(RecommendationsGenerated recommendationsGenerated) {
    log(recommendationsGenerated);

    rabbitTemplate.convertAndSend(
        RECOMMENDATION_NOTIFICATION_EXCHANGE_NAME, RECOMMENDATIONS_GENERATED_ROUTING_KEY,
        recommendationsGenerated);
  }

  private void log(
      RecommendationsGenerated recommendationsGenerated) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Sending notification about recommendation ready to bridge {}",
          recommendationsGenerated.getRecommendationInfosList().stream()
              .map(RecommendationInfo::getAlert)
              .collect(toList()));
    }
  }
}
