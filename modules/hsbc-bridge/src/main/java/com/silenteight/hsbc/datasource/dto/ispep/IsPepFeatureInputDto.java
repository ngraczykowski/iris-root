package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class IsPepFeatureInputDto {

  String feature;
  @Builder.Default
  List<IsPepFeatureSolutionDto> featureSolutions = Collections.emptyList();
}
