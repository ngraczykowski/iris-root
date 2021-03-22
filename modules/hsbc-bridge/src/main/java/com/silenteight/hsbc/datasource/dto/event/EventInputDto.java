package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class EventInputDto {

  String match;
  List<EventFeatureInputDto> featureInputs;
}
