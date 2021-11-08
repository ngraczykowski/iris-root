package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class EventInputResponse {

  @Builder.Default
  List<EventInputDto> inputs = Collections.emptyList();
}
