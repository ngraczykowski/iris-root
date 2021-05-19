package com.silenteight.serp.governance.agent;

import com.silenteight.serp.governance.agent.domain.dto.AgentDto;

import java.util.UUID;

import static com.silenteight.serp.governance.agent.domain.file.config.AgentConfigFixture.*;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.*;
import static java.util.Arrays.asList;
import static java.util.List.of;

public class AgentFixture {

  private static final String DOB_AGENT_ID = UUID.randomUUID().toString();
  private static final String DOCUMENT_AGENT_ID = UUID.randomUUID().toString();
  private static final String COUNTRY_AGENT_ID = UUID.randomUUID().toString();

  public static final AgentDto NAME_AGENT = AgentDto.builder()
      .id(NAME_AGENT_ID)
      .name(NAME_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_NAME))
      .solutions(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto DATE_AGENT = AgentDto.builder()
      .id(DATE_AGENT_ID)
      .name(DATE_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .solutions(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_WEAK_MATCH))
      .build();

  public static final AgentDto DOB_AGENT = AgentDto.builder()
      .id(DOB_AGENT_ID)
      .name(DOB_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .solutions(of(AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto DOCUMENT_AGENT = AgentDto.builder()
      .id(DOCUMENT_AGENT_ID)
      .name(DOCUMENT_AGENT_CONFIG_NAME)
      .features(asList(AGENT_FEATURE_DOCUMENT))
      .solutions(asList(AGENT_RESPONSE_PERFECT_MATCH, AGENT_RESPONSE_DIGIT_MATCH,
          AGENT_RESPONSE_WEAK_DIGIT_MATCH,
          AGENT_RESPONSE_WEAK_MATCH, AGENT_RESPONSE_NO_MATCH, AGENT_RESPONSE_NO_DATA))
      .build();

  public static final AgentDto COUNTRY_AGENT = AgentDto.builder()
      .id(COUNTRY_AGENT_ID)
      .name(COUNTRY_AGENT_CONFIG_NAME)
      .features(asList(AGENT_FEATURE_NATIONALITY, AGENT_FEATURE_RESIDENCY))
      .solutions(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_WEAK_MATCH, AGENT_RESPONSE_NO_DATA,
          AGENT_RESPONSE_NO_MATCH))
      .build();
}
