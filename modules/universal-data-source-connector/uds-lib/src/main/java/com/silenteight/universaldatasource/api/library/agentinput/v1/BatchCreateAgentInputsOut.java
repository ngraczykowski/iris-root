package com.silenteight.universaldatasource.api.library.agentinput.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchCreateAgentInputsOut {

  @Builder.Default
  List<CreatedAgentInputOut> createdAgentInputs = List.of();

  static BatchCreateAgentInputsOut createFrom(BatchCreateAgentInputsResponse inputs) {
    return BatchCreateAgentInputsOut.builder()
        .createdAgentInputs(inputs.getCreatedAgentInputsList()
            .stream()
            .map(CreatedAgentInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
