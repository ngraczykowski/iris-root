package com.silenteight.universaldatasource.api.library.commentinput.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.comments.api.v2.CommentInput;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
@Builder
public class CommentInputIn {

  String name;
  String alert;

  @Builder.Default
  Map<String, String> alertCommentInputs = Map.of();

  @Builder.Default
  List<MatchCommentInputIn> matchCommentInputs = List.of();

  CommentInput toCommentInput() {
    return CommentInput.newBuilder()
        .setName(name)
        .setAlert(alert)
        .setAlertCommentInput(StructMapperUtil.toStruct(alertCommentInputs))
        .addAllMatchCommentInputs(matchCommentInputs.stream()
            .map(MatchCommentInputIn::toMatchCommentInput)
            .collect(Collectors.toList())
        )
        .build();
  }
}
