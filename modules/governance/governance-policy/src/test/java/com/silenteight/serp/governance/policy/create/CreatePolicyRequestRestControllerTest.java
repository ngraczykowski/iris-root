package com.silenteight.serp.governance.policy.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.create.dto.CreatePolicyDto;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.PolicyState;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ CreatePolicyRequestRestController.class,
          CreatePolicyConfiguration.class,
          GenericExceptionControllerAdvice.class })
class CreatePolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_POLICY_URL = "/v1/policies";

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String POLICY_NAME = "policy_name";

  @MockBean
  private PolicyService policyService;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its202_whenPolicyAdded() {
    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, POLICY_NAME, PolicyState.DRAFT))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(policyService).addPolicy(POLICY_ID, POLICY_NAME, USERNAME);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, POLICY_NAME, PolicyState.DRAFT))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
