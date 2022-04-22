package com.silenteight.adjudication.engine.analysis.commentinput.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.commentinput.integration.CommentInputChannels.COMMENT_INPUT_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL;

@Component
@Slf4j
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "true"
)
class DummyCommentInputIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(COMMENT_INPUT_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .handle(PendingRecommendations.class, (payload, headers) -> {
          log.debug("New solving turn off {} DummyCommentInputIntegrationFlow");
          return null;
        });
  }
}
