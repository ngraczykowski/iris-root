package com.silenteight.serp.governance.agent.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.agent.domain.file.configuration.AgentDetailsConfigurationDto;
import com.silenteight.serp.governance.agent.domain.file.details.NonResolvableResourceException;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AgentDetailsResolver {

  private static final String AGENT_NAME_PREFIX = "agents/";
  private static final String AGENT_NAME_SEPARATOR = "/";
  private static final int AGENT_VERSION_PART_IDX = 3;
  private static final int AGENT_NAME_PART_IDX = 1;
  private static final String MALFORMED_AGENT_EXCEPTION_FORMAT = "Malformed agent name=%s";

  static String resolveAgentVersion(String agentName) {
    return resolvePart(agentName, AGENT_VERSION_PART_IDX);
  }

  private static String resolvePart(String agentName, int partIdx) {
    String part;
    try {
      String[] agentNameParts = agentName.split(AGENT_NAME_SEPARATOR);
      part = agentNameParts[partIdx];
    } catch (IndexOutOfBoundsException exception) {
      throw new NonResolvableResourceException(format(MALFORMED_AGENT_EXCEPTION_FORMAT, agentName));
    }
    return part;
  }

  static String resolveAgentName(String agentName) {
    return resolvePart(agentName, AGENT_NAME_PART_IDX);
  }

  static String resolveName(String agentId) {
    return AGENT_NAME_PREFIX + agentId;
  }

  static List<String> resolveConfigurations(List<AgentDetailsConfigurationDto> configurationDtos) {
    return configurationDtos
        .stream()
        .map(AgentDetailsConfigurationDto::getName)
        .collect(toList());
  }
}
