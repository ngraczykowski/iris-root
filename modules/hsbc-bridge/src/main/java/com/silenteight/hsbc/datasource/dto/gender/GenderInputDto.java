package com.silenteight.hsbc.datasource.dto.gender;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class GenderInputDto {

  String match;
  @Builder.Default
  List<GenderFeatureInputDto> featureInputs = Collections.emptyList();
}
