package com.silenteight.hsbc.datasource.dto.gender;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class GenderFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyGenders = Collections.emptyList();
  @Builder.Default
  List<String> watchlistGenders = Collections.emptyList();
}
