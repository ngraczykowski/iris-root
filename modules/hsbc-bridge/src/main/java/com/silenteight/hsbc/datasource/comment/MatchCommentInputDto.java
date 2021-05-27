package com.silenteight.hsbc.datasource.comment;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class MatchCommentInputDto {

  String match;
  Map<String, String> commentInput;
}
