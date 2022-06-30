package com.silenteight.agent.autoconfigure.grpc.metadata;

import org.jetbrains.annotations.NotNull;

public interface AgentInformationProvider {

  @NotNull String getName();

  @NotNull String getVersion();
}
