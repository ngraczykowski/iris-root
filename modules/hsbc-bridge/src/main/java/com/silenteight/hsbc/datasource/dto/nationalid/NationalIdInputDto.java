package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class NationalIdInputDto {

  String match;
  List<NationalIdFeatureInputDto> featureInputs;
}
