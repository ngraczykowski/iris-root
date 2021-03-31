package com.silenteight.hsbc.datasource.comment;

import lombok.Builder;
import lombok.Value;

import com.google.protobuf.Struct;

@Builder
@Value
public class MatchCommentInputDto {

  String match;
  Struct commentInput;
}
