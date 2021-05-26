package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class NameFeatureInputDto {

  String feature;
  @Builder.Default
  List<AlertedPartyNameDto> alertedPartyNames = emptyList();
  @Builder.Default
  List<WatchlistNameDto> watchlistNames = emptyList();
  EntityType alertedPartyType;
  @Builder.Default
  List<String> matchingTexts = emptyList();
}
