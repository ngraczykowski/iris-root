package com.silenteight.serp.governance.policy.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.create.dto.CreatePolicyDto;
import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.PolicyState.DRAFT;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.POLICY_NAME;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreatePolicyRequestRestController.class,
    CreatePolicyConfiguration.class,
    GenericExceptionControllerAdvice.class })
class CreatePolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_POLICY_URL = "/v1/policies";

  private static final UUID POLICY_ID = UUID.randomUUID();

  @MockBean
  private PolicyService policyService;

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.TestFixtures#getPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenPolicyAdded(String policyName) {
    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, policyName, DRAFT))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(policyService).createPolicy(POLICY_ID, policyName, USERNAME);
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, QA, USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, POLICY_NAME, DRAFT))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.TestFixtures#getIncorrectPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenPolicyNameLengthIsWrong(String policyName) {
    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, policyName, DRAFT))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }
}
