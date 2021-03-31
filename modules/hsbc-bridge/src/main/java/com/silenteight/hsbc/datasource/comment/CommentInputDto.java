package com.silenteight.hsbc.datasource.comment;

import lombok.Builder;
import lombok.Value;

import com.google.protobuf.Struct;

import java.util.List;

@Builder
@Value
public class CommentInputDto {

  String alert;
  Struct alertCommentInput;
  List<MatchCommentInputDto> matchCommentInputsDto;
}
