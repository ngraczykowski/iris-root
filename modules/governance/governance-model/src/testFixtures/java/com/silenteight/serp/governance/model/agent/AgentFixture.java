package com.silenteight.serp.governance.model.agent;

import static com.silenteight.serp.governance.model.agent.config.AgentConfigFixture.DATE_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.model.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_RESPONSE_MATCH;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_RESPONSE_NO_DATA;
import static java.util.List.of;

public class AgentFixture {

  public static final AgentDto NAME_AGENT = AgentDto.builder()
      .name(NAME_AGENT_CONFIG_NAME)
      .solutions(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto DATE_AGENT = AgentDto.builder()
      .name(DATE_AGENT_CONFIG_NAME)
      .solutions(of(AGENT_RESPONSE_NO_DATA))
      .build();
}
