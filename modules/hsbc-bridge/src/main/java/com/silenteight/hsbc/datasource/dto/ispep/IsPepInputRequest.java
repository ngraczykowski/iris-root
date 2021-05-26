package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class IsPepInputRequest {

  @Builder.Default
  List<String> matches = emptyList();
  @Builder.Default
  List<RegionModelFieldDto> regionModelFields = emptyList();
}
