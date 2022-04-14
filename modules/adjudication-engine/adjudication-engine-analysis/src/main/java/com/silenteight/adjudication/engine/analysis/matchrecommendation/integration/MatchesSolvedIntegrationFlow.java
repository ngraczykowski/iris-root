package com.silenteight.adjudication.engine.analysis.matchrecommendation.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchrecommendation.MatchRecommendationFacade;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchesSolvedEvent;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.EVENT_EXCHANGE_NAME;
import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.MATCH_RECOMMENDATIONS_GENERATED_ROUTING_KEY;

@RequiredArgsConstructor
@Slf4j
@Component
@ConditionalOnProperty(
    value = "ae.match-recommendation.flow.enabled",
    havingValue = "true"
)
public class MatchesSolvedIntegrationFlow {

  private final MatchRecommendationFacade matchRecommendationFacade;
  private final RabbitTemplate rabbitTemplate;

  @Async
  @EventListener
  public void receiveSolvedMatches(MatchesSolvedEvent event) {
    var matchesSolved = event.getMatchesSolved();
    log.debug("Received solved matches for generating match reommendation = {}", matchesSolved);
    var matchRecommendation = matchRecommendationFacade.generateMatchRecommendation(matchesSolved);
    if (matchRecommendation.isEmpty()) {
      return;
    }
    var message = new Message(matchRecommendation.get().toByteArray());
    rabbitTemplate.send(EVENT_EXCHANGE_NAME, MATCH_RECOMMENDATIONS_GENERATED_ROUTING_KEY, message);
  }
}
