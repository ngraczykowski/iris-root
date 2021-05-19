package com.silenteight.serp.governance.agent.domain.file.details;

import static com.silenteight.serp.governance.agent.domain.file.config.AgentConfigFixture.DATE_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.domain.file.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_DETAILS_CONF_DATE_INDV_NORMAL_DTO;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_DETAILS_CONF_DATE_NORMAL_DTO;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_DETAILS_CONF_DATE_STRICT_DTO;
import static java.util.List.of;

public class AgentDetailsFixture {

  public static final String NAME_AGENT_ID = "a9fbb313-b9f8-4792-99ad-6fdd74b9e629";
  public static final String DATE_AGENT_ID = "98cfd77c-41ac-4882-ab2a-d766891b92f7";
  public static final String DATE_AGENT_NAME = "agents/" + DATE_AGENT_ID;
  public static final String NAME_AGENT_NAME = "agents/" + NAME_AGENT_ID;
  public static final String DATE_AGENT_AGENT_NAME = "dateAgent";
  public static final String NAME_AGENT_AGENT_NAME = "nameAgent";
  public static final String AGENT_VERSION = "1.0.0";
  public static final String AGENT_FEATURE_NAME = "features/name";
  public static final String AGENT_FEATURE_DATE = "features/dob";
  public static final String AGENT_FEATURE_DOCUMENT = "features/dob";
  public static final String AGENT_FEATURE_NATIONALITY = "features/nationality";
  public static final String AGENT_FEATURE_RESIDENCY = "features/residency";
  public static final String AGENT_FEATURE_GENDER = "features/gender";
  public static final String AGENT_RESPONSE_MATCH = "MATCH";
  public static final String AGENT_RESPONSE_WEAK_MATCH = "WEAK_MATCH";
  public static final String AGENT_RESPONSE_NO_MATCH = "NO_MATCH";
  public static final String AGENT_RESPONSE_NO_DATA = "NO_DATA";
  public static final String AGENT_RESPONSE_PERFECT_MATCH = "PERFECT_MATCH";
  public static final String AGENT_RESPONSE_DIGIT_MATCH = "DIGIT_MATCH";
  public static final String AGENT_RESPONSE_WEAK_DIGIT_MATCH = "WEAK_DIGIT_MATCH";


  public static final AgentDetailDto NAME_AGENT_DETAIL_DTO = AgentDetailDto.builder()
      .id(NAME_AGENT_ID)
      .name(NAME_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_NAME))
      .responses(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA))
      .configurations(of())
      .build();

  public static final AgentDetailDto DATE_AGENT_DETAIL_DTO = AgentDetailDto.builder()
      .id(DATE_AGENT_ID)
      .name(DATE_AGENT_CONFIG_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .responses(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_WEAK_MATCH))
      .configurations(of(
          AGENT_DETAILS_CONF_DATE_NORMAL_DTO,
          AGENT_DETAILS_CONF_DATE_STRICT_DTO,
          AGENT_DETAILS_CONF_DATE_INDV_NORMAL_DTO))
      .build();
}
