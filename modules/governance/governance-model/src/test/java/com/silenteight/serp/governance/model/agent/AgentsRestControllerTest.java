package com.silenteight.serp.governance.model.agent;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_FEATURE_GENDER;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({AgentsRestController.class})
class AgentsRestControllerTest extends BaseRestControllerTest {

  private static final String ALL_AGENTS_URL = "/v1/features";
  private static final List<String> FEATURE_VALUE = List.of("MATCH", "NO_MATCH", "NO_DATA");

  @MockBean
  private AgentMappingService agentMappingService;

  @TestWithRole(roles = {POLICY_MANAGER})
  void its200_whenInvoked() {
    given(agentMappingService.getFeaturesListDto()).willReturn(prepareMockData());

    get(ALL_AGENTS_URL).statusCode(OK.value())
        .body("agents[0].name", is("features/gender"))
        .body("agents[0].solutions", is(FEATURE_VALUE));
  }

  @TestWithRole(roles = {APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR})
  void its403_whenNotPermittedRole() {
    get(ALL_AGENTS_URL).statusCode(FORBIDDEN.value());
  }


  private FeaturesListDto prepareMockData() {
    FeatureDto agent1 = FeatureDto.builder()
        .name(AGENT_FEATURE_GENDER)
        .solutions(FEATURE_VALUE)
        .build();

    return FeaturesListDto.builder()
        .agents(asList(agent1))
        .build();
  }
}