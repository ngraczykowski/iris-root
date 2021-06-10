package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class IsPepFeatureInputDto {

  String feature;
  @Builder.Default
  List<IsPepFeatureSolutionDto> featureSolutions = emptyList();
}
