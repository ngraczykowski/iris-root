package com.silenteight.adjudication.engine.comments.commentinput;

import lombok.Builder;
import lombok.Value;

import com.google.protobuf.Struct;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class CommentInputResponse {

  String alert;

  Struct alertCommentInput;

  List<MatchCommentInputResponse> matchCommentInput;

  public static CommentInputResponse fromCommentInputV1(
      com.silenteight.datasource.comments.api.v1.CommentInput commentInput) {
    return CommentInputResponse
        .builder()
        .alert(commentInput.getAlert())
        .alertCommentInput(commentInput.getAlertCommentInput())
        .matchCommentInput(commentInput
            .getMatchCommentInputsList()
            .stream()
            .map(MatchCommentInputResponse::fromMatchCommentInputV1)
            .collect(toList()))
        .build();
  }

  public static CommentInputResponse fromCommentInputV2(
      com.silenteight.datasource.comments.api.v2.CommentInput commentInput) {
    return CommentInputResponse
        .builder()
        .alert(commentInput.getAlert())
        .alertCommentInput(commentInput.getAlertCommentInput())
        .matchCommentInput(commentInput
            .getMatchCommentInputsList()
            .stream()
            .map(MatchCommentInputResponse::fromMatchCommentInputV2)
            .collect(toList()))
        .build();
  }
}
