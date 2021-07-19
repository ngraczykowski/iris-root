package com.silenteight.hsbc.datasource.dto.allowedlist;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class AllowListFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> characteristicsValues = emptyList();
  List<String> allowListNames;
}
