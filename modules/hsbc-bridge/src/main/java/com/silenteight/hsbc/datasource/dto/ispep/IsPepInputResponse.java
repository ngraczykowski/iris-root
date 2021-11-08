package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class IsPepInputResponse {

  String match;
  @Builder.Default
  List<IsPepFeatureInputDto> featureInputs = Collections.emptyList();
}
