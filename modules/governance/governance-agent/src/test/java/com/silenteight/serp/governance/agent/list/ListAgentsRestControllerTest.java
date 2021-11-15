package com.silenteight.serp.governance.agent.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.agent.list.ListAgentFixture.LIST_AGENT_DATE_DTO;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListAgentsRestController.class })
class ListAgentsRestControllerTest extends BaseRestControllerTest {

  private static final String AGENTS_DETAILS_URL = "/v1/agents/";

  @MockBean
  private ListAgentQuery listAgentQuery;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenInvoked() {
    given(listAgentQuery.list()).willReturn(List.of(LIST_AGENT_DATE_DTO));

    get(AGENTS_DETAILS_URL).statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(LIST_AGENT_DATE_DTO.getId()))
        .body("[0].name", is(LIST_AGENT_DATE_DTO.getName()))
        .body("[0].agentName", is(LIST_AGENT_DATE_DTO.getAgentName()))
        .body("[0].agentVersion", is(LIST_AGENT_DATE_DTO.getAgentVersion()))
        .body(
            "[0].featuresList[0].name", is(LIST_AGENT_DATE_DTO.getFeaturesList().get(0).getName()));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(AGENTS_DETAILS_URL).statusCode(FORBIDDEN.value());
  }
}
