package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;


@Builder
@Value
public class NameInputDto {

  String match;
  @Builder.Default
  List<NameFeatureInputDto> featureInputs = Collections.emptyList();
}
