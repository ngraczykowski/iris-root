package com.silenteight.adjudication.engine.solving.domain.command;

import lombok.Value;

import java.util.Map;

@Value
public class UpdateCommentInput {

  private long matchId;
  private Map<String, Object> comments;
}
