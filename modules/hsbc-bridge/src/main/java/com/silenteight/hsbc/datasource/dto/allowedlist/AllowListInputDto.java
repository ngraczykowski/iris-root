package com.silenteight.hsbc.datasource.dto.allowedlist;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class AllowListInputDto {

  String match;

  @Builder.Default
  List<AllowListFeatureInputDto> featureInputs = Collections.emptyList();
}
