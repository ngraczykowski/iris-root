package com.silenteight.universaldatasource.api.library.event.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.event.v1.EventInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class EventInputOut {

  String match;

  @Builder.Default
  List<EventFeatureOut> eventFeatureInputs = List.of();

  static EventInputOut createFrom(EventInput input) {
    return EventInputOut.builder()
        .match(input.getMatch())
        .eventFeatureInputs(
            input.getEventFeatureInputsList().stream()
                .map(EventFeatureOut::createFrom)
                .collect(Collectors.toList()))
        .build();
  }
}
