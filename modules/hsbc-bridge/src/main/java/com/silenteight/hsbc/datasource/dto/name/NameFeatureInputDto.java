package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class NameFeatureInputDto {

  String feature;
  List<AlertedPartyNameDto> alertedPartyNames;
  List<WatchlistNameDto> watchlistNames;
  EntityType alertedPartyType;
  List<String> matchingTexts;
}
