package com.silenteight.adjudication.engine.analysis.recommendation.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchesSolvedEvent;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.EVENT_EXCHANGE_NAME;
import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.RECOMMENDATIONS_GENERATED_ROUTING_KEY;

@RequiredArgsConstructor
@Component
@Slf4j
class MatchesSolvedRecommendationIntegrationFlow {

  private final RecommendationFacade facade;
  private final RabbitTemplate rabbitTemplate;

  @Async
  @EventListener
  public void receiveSolvedMatches(MatchesSolvedEvent event) {
    var matchesSolved = event.getMatchesSolved();
    log.debug("Received solved matches for generating recommendation = {}", matchesSolved);
    var recommendation = facade.handleMatchesSolved(matchesSolved);
    if (recommendation.isEmpty()) {
      log.info("MatchesSolvedRecommendationIntegrationFlow recommendation empty");
      return;
    }
    log.info(
        "Sending recommendation for analysis:{}", recommendation.get().getAnalysis());
    var message = new Message(recommendation.get().toByteArray());
    rabbitTemplate.send(EVENT_EXCHANGE_NAME, RECOMMENDATIONS_GENERATED_ROUTING_KEY, message);
  }
}
