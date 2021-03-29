package com.silenteight.serp.governance.model.agent;

import static com.silenteight.serp.governance.model.agent.config.AgentConfigFixture.*;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.*;
import static java.util.Arrays.asList;
import static java.util.List.of;

public class AgentFixture {

  public static final AgentDto NAME_AGENT = AgentDto.builder()
      .name(NAME_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_NAME))
      .solutions(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto DATE_AGENT = AgentDto.builder()
      .name(DATE_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .solutions(of(AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto DOB_AGENT = AgentDto.builder()
      .name(DOB_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .solutions(of(AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto DOCUMENT_AGENT = AgentDto.builder()
      .name(DOCUMENT_AGENT_CONFIG_NAME)
      .features(asList(AGENT_FEATURE_DOCUMENT))
      .solutions(asList(AGENT_RESPONSE_PERFECT_MATCH, AGENT_RESPONSE_DIGIT_MATCH,
          AGENT_RESPONSE_WEAK_DIGIT_MATCH,
          AGENT_RESPONSE_WEAK_MATCH, AGENT_RESPONSE_NO_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto COUNTRY_AGENT = AgentDto.builder()
      .name(COUNTRY_AGENT_CONFIG_NAME)
      .features(asList(AGENT_FEATURE_NATIONALITY, AGENT_FEATURE_RESIDENCY))
      .solutions(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_WEAK_MATCH, AGENT_RESPONSE_NO_DATA,
          AGENT_RESPONSE_NO_MATCH))
      .build();
}
