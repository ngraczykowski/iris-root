package com.silenteight.adjudication.engine.analysis.commentinput.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputFacade;
import com.silenteight.adjudication.internal.v1.CommentInputsUpdated;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.commentinput.integration.CommentInputChannels.COMMENT_INPUTS_UPDATED_OUTBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.commentinput.integration.CommentInputChannels.COMMENT_INPUT_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL;


@RequiredArgsConstructor
@Component
@Slf4j
class CommentInputIntegrationFlow extends IntegrationFlowAdapter {

  private final CommentInputFacade commentInputFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(
        COMMENT_INPUT_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .handle(PendingRecommendations.class, (payload, headers) -> {
          commentInputFacade.handlePendingRecommendations(payload);
          return CommentInputsUpdated
              .newBuilder()
              .addAllAnalysis(payload.getAnalysisList())
              .build();
        })
        .channel(COMMENT_INPUTS_UPDATED_OUTBOUND_CHANNEL);
  }
}
