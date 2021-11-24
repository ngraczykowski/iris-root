package com.silenteight.hsbc.datasource.comment;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
class CommentInputDto {

  String name;
  String alert;
  Map<String, String> alertCommentInput;
  List<MatchCommentInputDto> matchCommentInputsDto;
}
