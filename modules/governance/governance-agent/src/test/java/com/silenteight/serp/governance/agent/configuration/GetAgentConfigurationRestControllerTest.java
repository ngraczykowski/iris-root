package com.silenteight.serp.governance.agent.configuration;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.agent.domain.AgentsRegistry;

import io.restassured.http.ContentType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_INDV_NORMAL;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.DATE_INDV_NORMAL_OUTPUT;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.DATE_AGENT_ID;
import static java.lang.String.format;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ GetAgentConfigurationRestController.class })
class GetAgentConfigurationRestControllerTest extends BaseRestControllerTest {

  private static final String AGENTS_CONFIGURATION_URL = format("/v1/agents/%s/configuration/%s",
      DATE_AGENT_ID,
      AGENT_CONF_DATE_INDV_NORMAL);

  @MockBean
  private AgentsRegistry agentsRegistry;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR, QA })
  void its200_whenInvoked() {

    given(agentsRegistry.getAgentConfigurationDetails(DATE_AGENT_ID, AGENT_CONF_DATE_INDV_NORMAL))
        .willReturn(DATE_INDV_NORMAL_OUTPUT);

    get(AGENTS_CONFIGURATION_URL)
        .statusCode(OK.value())
        .contentType(ContentType.JSON)
        .body("solvers.date", is(not(empty())))
        .body("'solvers.date'.generation.min-year", is(1900))
        .body("'solvers.date'.generation.max-year", is(2100))
        .body("'solvers.date'.boundary.min-year", is(1902))
        .body("'solvers.date'.delimiters", is(of(" ", ",")))
        .body("'solvers.date'.inconclusivePatterns", is(of(" to ")));
    verify(agentsRegistry, times(1))
        .getAgentConfigurationDetails(DATE_AGENT_ID, AGENT_CONF_DATE_INDV_NORMAL);
  }

  @TestWithRole(roles = { QA_ISSUE_MANAGER, USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(AGENTS_CONFIGURATION_URL).statusCode(FORBIDDEN.value());
  }
}