package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class EventInputDto {

  String match;
  @Builder.Default
  List<EventFeatureInputDto> featureInputs = emptyList();
}
