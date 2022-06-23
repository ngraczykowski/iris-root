package com.silenteight.serp.governance.agent.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.DETAILS_DATE_AGENT_DTO;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.AGENT_FEATURE_DATE;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.DATE_AGENT_ID;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ GetAgentDetailsRestController.class })
class GetAgentDetailsRestControllerTest extends BaseRestControllerTest {

  private static final String AGENTS_DETAILS_URL = format("/v1/agents/%s", DATE_AGENT_ID);

  @MockBean
  private AgentDetailsQuery agentDetailsQuery;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenInvoked() {
    given(agentDetailsQuery.details(DATE_AGENT_ID)).willReturn(DETAILS_DATE_AGENT_DTO);

    get(AGENTS_DETAILS_URL).statusCode(OK.value())
        .body("id", is(DETAILS_DATE_AGENT_DTO.getId()))
        .body("name", is(DETAILS_DATE_AGENT_DTO.getName()))
        .body("agentName", is(DETAILS_DATE_AGENT_DTO.getAgentName()))
        .body("agentVersion", is(DETAILS_DATE_AGENT_DTO.getAgentVersion()))
        .body("features", is(DETAILS_DATE_AGENT_DTO.getFeatures()))
        .body("featuresList[0].name", is(AGENT_FEATURE_DATE))
        .body("responses", is(DETAILS_DATE_AGENT_DTO.getResponses()))
        .body("configurations", is(DETAILS_DATE_AGENT_DTO.getConfigurations()));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(AGENTS_DETAILS_URL).statusCode(FORBIDDEN.value());
  }

}
