package com.silenteight.serp.governance.agent.list;

import com.silenteight.serp.governance.agent.list.dto.ListAgentDto;

import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.*;
import static java.util.List.of;

public class ListAgentFixture {

  public static final ListAgentDto LIST_AGENT_NAME_DTO = ListAgentDto.builder()
      .id(NAME_AGENT_ID)
      .agentName(NAME_AGENT_AGENT_NAME)
      .agentVersion(AGENT_VERSION)
      .name(NAME_AGENT_NAME)
      .features(of(AGENT_FEATURE_NAME))
      .featuresList(of(AGENT_FEATURE_NAME_DTO))
      .build();

  public static final ListAgentDto LIST_AGENT_DATE_DTO = ListAgentDto.builder()
      .id(DATE_AGENT_ID)
      .agentName(DATE_AGENT_AGENT_NAME)
      .agentVersion(AGENT_VERSION)
      .name(DATE_AGENT_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .featuresList(of(AGENT_FEATURE_DATE_DTO))
      .build();
}
