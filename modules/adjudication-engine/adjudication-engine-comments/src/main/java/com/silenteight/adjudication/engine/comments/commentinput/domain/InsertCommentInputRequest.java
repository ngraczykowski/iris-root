package com.silenteight.adjudication.engine.comments.commentinput.domain;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value
@Builder
public class InsertCommentInputRequest {

  Long alertId;

  ObjectNode value;
}
