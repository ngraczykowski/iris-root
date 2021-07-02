package com.silenteight.hsbc.datasource.dto.allowedlist;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class AllowListInputDto {

  String match;

  @Builder.Default
  List<AllowListFeatureInputDto> featureInputs = emptyList();
}
