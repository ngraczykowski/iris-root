package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class IsPepInputDto {

  String match;
  @Builder.Default
  List<IsPepFeatureInputDto> featureInputs = emptyList();
}
