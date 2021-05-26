package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class NationalIdInputDto {

  String match;
  @Builder.Default
  List<NationalIdFeatureInputDto> featureInputs = emptyList();
}
