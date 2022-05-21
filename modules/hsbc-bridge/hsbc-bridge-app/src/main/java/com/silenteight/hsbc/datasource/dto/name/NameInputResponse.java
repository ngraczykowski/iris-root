package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class NameInputResponse {

  @Builder.Default
  List<NameInputDto> inputs = Collections.emptyList();
}
