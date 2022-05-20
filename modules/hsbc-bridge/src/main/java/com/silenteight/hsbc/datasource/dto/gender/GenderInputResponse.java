package com.silenteight.hsbc.datasource.dto.gender;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class GenderInputResponse {

  @Builder.Default
  List<GenderInputDto> inputs = Collections.emptyList();
}
