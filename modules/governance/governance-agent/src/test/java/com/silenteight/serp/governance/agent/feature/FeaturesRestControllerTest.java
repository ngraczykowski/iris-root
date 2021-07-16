package com.silenteight.serp.governance.agent.feature;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.agent.domain.FeaturesProvider;
import com.silenteight.serp.governance.agent.domain.dto.FeatureDto;
import com.silenteight.serp.governance.agent.domain.dto.FeaturesListDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.AGENT_FEATURE_GENDER;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ FeaturesRestController.class })
class FeaturesRestControllerTest extends BaseRestControllerTest {

  private static final String ALL_AGENTS_URL = "/v1/features";
  private static final List<String> FEATURE_VALUE = of("MATCH", "NO_MATCH", "NO_DATA");
  private static final String AGENT_CONFIG = "agents/name/versions/1.0.0/configs/1";

  @MockBean
  private FeaturesProvider featuresProvider;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenInvoked() {
    given(featuresProvider.getFeaturesListDto()).willReturn(prepareMockData());

    get(ALL_AGENTS_URL).statusCode(OK.value())
        .body("features[0].name", is("features/gender"))
        .body("features[0].solutions", is(FEATURE_VALUE));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(ALL_AGENTS_URL).statusCode(FORBIDDEN.value());
  }

  private FeaturesListDto prepareMockData() {
    FeatureDto agent1 = FeatureDto.builder()
        .name(AGENT_FEATURE_GENDER)
        .solutions(FEATURE_VALUE)
        .agentConfig(AGENT_CONFIG)
        .build();

    return FeaturesListDto.builder()
        .features(of(agent1))
        .build();
  }
}
