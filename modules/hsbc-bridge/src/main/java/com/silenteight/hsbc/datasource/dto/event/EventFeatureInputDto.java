package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class EventFeatureInputDto {

  String feature;
  List<String> alertedPartyDates;
  List<String> watchlistEvents;
}
