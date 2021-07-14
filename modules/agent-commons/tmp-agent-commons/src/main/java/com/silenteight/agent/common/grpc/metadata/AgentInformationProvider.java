package com.silenteight.agent.common.grpc.metadata;

import org.jetbrains.annotations.NotNull;

public interface AgentInformationProvider {

  @NotNull String getName();

  @NotNull String getVersion();
}
