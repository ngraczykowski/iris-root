package com.silenteight.serp.governance.model.agent.details;

import static java.util.List.of;

public class AgentDetailsFixture {

  public static final String AGENT_FEATURE_NAME = "features/name";
  public static final String AGENT_FEATURE_DATE = "features/dob";
  public static final String AGENT_RESPONSE_MATCH = "MATCH";
  public static final String AGENT_RESPONSE_NO_DATA = "NO_DATA";

  public static final AgentDetailDto NAME_AGENT_DETAIL = AgentDetailDto.builder()
      .features(of(AGENT_FEATURE_NAME))
      .responses(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDetailDto DATE_AGENT_DETAIL = AgentDetailDto.builder()
      .features(of(AGENT_FEATURE_DATE))
      .responses(of(AGENT_RESPONSE_NO_DATA))
      .build();
}
