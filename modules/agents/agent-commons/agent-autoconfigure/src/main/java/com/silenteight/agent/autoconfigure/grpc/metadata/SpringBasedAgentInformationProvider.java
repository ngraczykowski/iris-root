package com.silenteight.agent.autoconfigure.grpc.metadata;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.info.BuildProperties;

@RequiredArgsConstructor
public class SpringBasedAgentInformationProvider implements AgentInformationProvider {

  private final BuildProperties buildProperties;

  @Override
  public @NotNull String getName() {
    return buildProperties.getName();
  }

  @Override
  public @NotNull String getVersion() {
    return buildProperties.getVersion();
  }
}
