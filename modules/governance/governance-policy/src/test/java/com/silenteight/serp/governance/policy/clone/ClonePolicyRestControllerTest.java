package com.silenteight.serp.governance.policy.clone;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ClonePolicyRequest;
import com.silenteight.serp.governance.policy.domain.exception.WrongBasePolicyException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Import({ ClonePolicyRestController.class,
          ClonePolicyConfiguration.class,
          GenericExceptionControllerAdvice.class,
          ClonePolicyControllerAdvice.class })
class ClonePolicyRestControllerTest extends BaseRestControllerTest {

  private static final String CLONE_POLICY_URL = "/v1/policies/%s/clone/policies/%s";

  private static final UUID POLICY_ID = randomUUID();
  private static final UUID POLICY_ID_CLONED = randomUUID();

  @MockBean
  private PolicyService policyService;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its202_whenPolicyUseCaseUsed() {

    post(String.format(CLONE_POLICY_URL, POLICY_ID, POLICY_ID_CLONED))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    ArgumentCaptor<ClonePolicyRequest> captor = ArgumentCaptor.forClass(ClonePolicyRequest.class);
    verify(policyService).clonePolicy(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID_CLONED);
    assertThat(captor.getValue().getBasePolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getCreatedBy()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    post(String.format(CLONE_POLICY_URL, POLICY_ID, POLICY_ID_CLONED))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its422_whenPolicyIdAlreadyExists() {
    WrongBasePolicyException exception = new WrongBasePolicyException(POLICY_ID);
    Mockito.when(policyService.clonePolicy(any())).thenThrow(exception);
    post(String.format(CLONE_POLICY_URL, POLICY_ID, POLICY_ID_CLONED))
        .contentType(anything())
        .statusCode(UNPROCESSABLE_ENTITY.value())
        .body(containsString(exception.getMessage()));
  }
}
