package com.silenteight.hsbc.datasource.comment;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class CommentInputDto {

  String alert;
  Map<String, String> alertCommentInput;
  List<MatchCommentInputDto> matchCommentInputsDto;
}
