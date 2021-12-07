package com.silenteight.universaldatasource.api.library.commentinput.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputRequest;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchCreateCommentInputIn {

  @Builder.Default
  List<CommentInputIn> commentInputs = List.of();

  BatchCreateCommentInputRequest toBatchCreateCommentInputRequest() {
    return BatchCreateCommentInputRequest
        .newBuilder()
        .addAllCommentInputs(commentInputs.stream()
            .map(CommentInputIn::toCommentInput)
            .collect(Collectors.toList()))
        .build();
  }
}
