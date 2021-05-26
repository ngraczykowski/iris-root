package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class NameInputDto {

  String match;
  @Builder.Default
  List<NameFeatureInputDto> featureInputs = emptyList();
}
