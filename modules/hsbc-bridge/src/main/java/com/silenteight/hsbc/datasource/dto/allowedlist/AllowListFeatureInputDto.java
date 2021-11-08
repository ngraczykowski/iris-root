package com.silenteight.hsbc.datasource.dto.allowedlist;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class AllowListFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> characteristicsValues = Collections.emptyList();
  List<String> allowListNames;
}
