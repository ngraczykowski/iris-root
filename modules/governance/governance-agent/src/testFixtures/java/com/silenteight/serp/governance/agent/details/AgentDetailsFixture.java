package com.silenteight.serp.governance.agent.details;

import com.silenteight.serp.governance.agent.details.dto.AgentDetailsDto;

import java.util.List;

import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_ENT_NORMAL;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_ENT_STRICT;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_INDV_NORMAL;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.*;

public class AgentDetailsFixture {

  public static final AgentDetailsDto DETAILS_DATE_AGENT_DTO = AgentDetailsDto.builder()
      .id(DATE_AGENT_ID)
      .agentName(DATE_AGENT_AGENT_NAME)
      .agentVersion(AGENT_VERSION)
      .name(DATE_AGENT_NAME)
      .features(DATE_AGENT_DETAIL_DTO.getFeatures())
      .configurations(List.of(
          AGENT_CONF_DATE_ENT_NORMAL,
          AGENT_CONF_DATE_ENT_STRICT,
          AGENT_CONF_DATE_INDV_NORMAL))
      .responses(List.of(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_WEAK_MATCH))
      .build();
}
