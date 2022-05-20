package com.silenteight.adjudication.engine.comments.commentinput;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CommentInputFixture {

  public static AlertCommentInput newAlertInputFixture(long alertId, ObjectNode value) {
    return AlertCommentInput.builder()
        .id(1L)
        .alertId(alertId)
        .value(value)
        .build();
  }
}
