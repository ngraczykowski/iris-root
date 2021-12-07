package com.silenteight.universaldatasource.api.library.commentinput.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.comments.api.v2.MatchCommentInput;

import java.util.Map;

@Value
@Builder
public class MatchCommentInputIn {

  String match;

  @Builder.Default
  Map<String, String> commentInput = Map.of();

  MatchCommentInput toMatchCommentInput() {
    return MatchCommentInput.newBuilder()
        .setMatch(match)
        .setCommentInput(StructMapperUtil.toStruct(commentInput))
        .build();
  }
}
