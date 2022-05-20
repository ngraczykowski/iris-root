package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class NameFeatureInputDto {

  String feature;
  @Builder.Default
  List<AlertedPartyNameDto> alertedPartyNames = Collections.emptyList();
  @Builder.Default
  List<WatchlistNameDto> watchlistNames = Collections.emptyList();
  EntityType alertedPartyType;
  @Builder.Default
  List<String> matchingTexts = Collections.emptyList();
}
