package com.silenteight.adjudication.engine.comments.commentinput;

import lombok.Builder;
import lombok.Value;

import com.google.protobuf.Struct;

@Value
@Builder
public class MatchCommentInputResponse {

  String match;

  Struct commentInput;

  public static MatchCommentInputResponse fromMatchCommentInputV1(
      com.silenteight.datasource.comments.api.v1.MatchCommentInput matchCommentInput) {
    return MatchCommentInputResponse
        .builder()
        .match(matchCommentInput.getMatch())
        .commentInput(matchCommentInput.getCommentInput())
        .build();
  }

  public static MatchCommentInputResponse fromMatchCommentInputV2(
      com.silenteight.datasource.comments.api.v2.MatchCommentInput matchCommentInput) {
    return MatchCommentInputResponse
        .builder()
        .match(matchCommentInput.getMatch())
        .commentInput(matchCommentInput.getCommentInput())
        .build();
  }
}
