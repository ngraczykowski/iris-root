package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class EventFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDates = emptyList();
  @Builder.Default
  List<String> watchlistEvents = emptyList();
}
