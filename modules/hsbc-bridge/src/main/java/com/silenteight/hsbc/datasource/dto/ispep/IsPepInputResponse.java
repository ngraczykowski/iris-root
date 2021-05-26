package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class IsPepInputResponse {

  @Builder.Default
  List<IsPepInputDto> inputs = emptyList();
}
