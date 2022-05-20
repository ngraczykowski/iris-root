package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class EventFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDates = Collections.emptyList();
  @Builder.Default
  List<String> watchlistEvents = Collections.emptyList();
}
