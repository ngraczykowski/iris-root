package com.silenteight.universaldatasource.api.library.agentinput.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.universaldatasource.api.library.Feature;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchCreateAgentInputsIn<T extends Feature> {

  @Builder.Default
  List<AgentInputIn<T>> agentInputs = List.of();

  BatchCreateAgentInputsRequest toBatchCreateAgentInputsRequest() {
    return BatchCreateAgentInputsRequest.newBuilder()
        .addAllAgentInputs(
            agentInputs.stream()
                .map(AgentInputIn::createFrom)
                .collect(Collectors.toList())
        ).build();
  }
}
