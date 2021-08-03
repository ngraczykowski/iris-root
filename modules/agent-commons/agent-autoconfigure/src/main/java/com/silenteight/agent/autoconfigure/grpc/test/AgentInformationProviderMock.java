package com.silenteight.agent.autoconfigure.grpc.test;

import lombok.Value;

import com.silenteight.agent.autoconfigure.grpc.metadata.AgentInformationProvider;

@Value
public class AgentInformationProviderMock implements AgentInformationProvider {

  String name;
  String version;
}
