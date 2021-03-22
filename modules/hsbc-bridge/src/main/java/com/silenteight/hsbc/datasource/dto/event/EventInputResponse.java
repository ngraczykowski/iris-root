package com.silenteight.hsbc.datasource.dto.event;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class EventInputResponse {

  List<EventInputDto> inputs;
}
