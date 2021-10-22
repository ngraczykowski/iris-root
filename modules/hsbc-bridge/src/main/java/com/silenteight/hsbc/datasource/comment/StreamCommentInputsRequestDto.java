package com.silenteight.hsbc.datasource.comment;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import javax.validation.constraints.NotNull;

@Builder
@Value
class StreamCommentInputsRequestDto {

  @NotNull
  List<String> alerts;
}
