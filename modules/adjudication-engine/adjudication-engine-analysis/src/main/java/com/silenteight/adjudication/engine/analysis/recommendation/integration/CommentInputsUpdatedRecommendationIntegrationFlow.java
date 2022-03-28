package com.silenteight.adjudication.engine.analysis.recommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.internal.v1.CommentInputsUpdated;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(
    value = "ae.recommendation.comment-input-updated.enabled",
    havingValue = "true"
)
class CommentInputsUpdatedRecommendationIntegrationFlow extends IntegrationFlowAdapter {

  private final RecommendationFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(RecommendationChannels.COMMENT_INPUT_UPDATED_RECOMMENDATION_INBOUND_CHANNEL)
        .handle(
            CommentInputsUpdated.class,
            (payload, headers) -> facade.handleCommentInputsUpdated(payload))
        .split()
        .log(Level.TRACE, getClass().getName())
        .channel(RecommendationChannels.RECOMMENDATIONS_GENERATED_OUTBOUND_CHANNEL);
  }
}
