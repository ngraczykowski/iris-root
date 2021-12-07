package com.silenteight.universaldatasource.api.library.event.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchEventInputsOut {

  @Builder.Default
  List<EventInputOut> eventInputs = List.of();

  static BatchGetMatchEventInputsOut createFrom(BatchGetMatchEventInputsResponse response) {
    return BatchGetMatchEventInputsOut.builder()
        .eventInputs(response.getEventInputsList().stream()
            .map(EventInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
