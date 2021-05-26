package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class EventInputResponse {

  @Builder.Default
  List<EventInputDto> inputs = emptyList();
}
