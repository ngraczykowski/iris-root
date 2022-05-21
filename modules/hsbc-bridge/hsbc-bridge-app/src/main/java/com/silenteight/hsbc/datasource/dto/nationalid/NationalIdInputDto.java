package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class NationalIdInputDto {

  String match;
  @Builder.Default
  List<NationalIdFeatureInputDto> featureInputs = Collections.emptyList();
}
