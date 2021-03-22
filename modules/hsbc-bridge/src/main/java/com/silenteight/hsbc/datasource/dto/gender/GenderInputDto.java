package com.silenteight.hsbc.datasource.dto.gender;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class GenderInputDto {

  String match;
  List<GenderFeatureInputDto> featureInputs;
}
