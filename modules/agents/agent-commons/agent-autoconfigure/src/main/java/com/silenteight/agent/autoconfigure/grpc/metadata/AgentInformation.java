package com.silenteight.agent.autoconfigure.grpc.metadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class AgentInformation {

  @NonNull
  private final String name;
  @NonNull
  private final String version;

  static AgentInformation fromProvider(AgentInformationProvider provider) {
    var name = provider.getName();
    var version = provider.getVersion();

    if (isBlank(name) || isBlank(version)) {
      throw new MissingAgentInformation();
    }

    return new AgentInformation(name, version);
  }

  private static class MissingAgentInformation extends RuntimeException {

    private static final long serialVersionUID = -9060425380418977104L;

    MissingAgentInformation() {
      super("Agent information used for enhancing response metadata is missing.");
    }
  }
}
