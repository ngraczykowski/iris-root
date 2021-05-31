package com.silenteight.adjudication.engine.analysis.commentinput.integration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentInputChannels {

  public static final String COMMENT_INPUT_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL =
      "commentInputPendingRecommendationsInboundChannel";

  public static final String COMMENT_INPUTS_UPDATED_OUTBOUND_CHANNEL =
      "commentInputsUpdatedOutboundChannel";
}
