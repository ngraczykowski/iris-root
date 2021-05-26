package com.silenteight.hsbc.datasource.dto.gender;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class GenderFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyGenders = emptyList();
  @Builder.Default
  List<String> watchlistGenders = emptyList();
}
