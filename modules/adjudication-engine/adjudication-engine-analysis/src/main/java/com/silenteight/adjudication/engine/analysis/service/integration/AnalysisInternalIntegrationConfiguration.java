package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.commentinput.integration.CommentInputChannels;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchesSolvedEvent;
import com.silenteight.adjudication.engine.analysis.matchsolution.integration.MatchSolutionChannels;
import com.silenteight.adjudication.engine.analysis.recommendation.integration.RecommendationChannels;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@RequiredArgsConstructor
class AnalysisInternalIntegrationConfiguration {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Bean
  IntegrationFlow matchesSolvedToRecommendationIntegrationFlow() {
    return from(MatchSolutionChannels.MATCHES_SOLVED_OUTBOUND_CHANNEL)
        .handle(
            MatchesSolved.class,
            (p, h) -> {
              applicationEventPublisher.publishEvent(new MatchesSolvedEvent(p));
              return p;
            })
        .log(Level.TRACE, getClass().getName() + ".matchesSolvedToRecommendationIntegrationFlow")
        .get();
  }

  @Bean
  IntegrationFlow commentInputToRecommendationIntegrationFlow() {
    return from(CommentInputChannels.COMMENT_INPUTS_UPDATED_OUTBOUND_CHANNEL)
        .log(Level.TRACE, getClass().getName() + ".commentInputToRecommendationIntegrationFlow")
        .channel(RecommendationChannels.COMMENT_INPUT_UPDATED_RECOMMENDATION_INBOUND_CHANNEL)
        .get();
  }
}
