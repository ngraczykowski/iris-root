package com.silenteight.universaldatasource.api.library.agentinput.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;

@Value
@Builder
public class CreatedAgentInputOut {

  String name;
  String match;

  static CreatedAgentInputOut createFrom(CreatedAgentInput input) {
    return CreatedAgentInputOut.builder()
        .match(input.getMatch())
        .name(input.getName())
        .build();
  }
}
