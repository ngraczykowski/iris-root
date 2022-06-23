package com.silenteight.serp.governance.agent.details;

import com.silenteight.serp.governance.agent.details.dto.AgentDetailsDto;

import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_ENT_NORMAL;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_ENT_STRICT;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_INDV_NORMAL;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.*;
import static java.util.List.of;

public class AgentDetailsFixture {

  public static final AgentDetailsDto DETAILS_DATE_AGENT_DTO = AgentDetailsDto.builder()
      .id(DATE_AGENT_ID)
      .agentName(DATE_AGENT_AGENT_NAME)
      .agentVersion(AGENT_VERSION)
      .name(DATE_AGENT_NAME)
      .features(of(AGENT_FEATURE_DATE))
      .featuresList(DATE_AGENT_DETAIL_DTO.getFeaturesList())
      .configurations(of(
          AGENT_CONF_DATE_ENT_NORMAL,
          AGENT_CONF_DATE_ENT_STRICT,
          AGENT_CONF_DATE_INDV_NORMAL))
      .responses(of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_WEAK_MATCH))
      .build();
}
