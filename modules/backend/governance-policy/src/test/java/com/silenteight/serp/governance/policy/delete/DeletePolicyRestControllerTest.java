package com.silenteight.serp.governance.policy.delete;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.DeletePolicyRequest;
import com.silenteight.serp.governance.policy.domain.exception.WrongPolicyStateException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Import({ DeletePolicyRestController.class,
          DeletePolicyConfiguration.class,
          DeletePolicyControllerAdvice.class,
          GenericExceptionControllerAdvice.class })
class DeletePolicyRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = randomUUID();

  private static final String DELETE_POLICY_URL = "/v1/policies/";
  private static final String DELETE_URL = DELETE_POLICY_URL + POLICY_ID;

  @MockBean
  private PolicyService policyService;

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenPolicyDeleted() {
    delete(DELETE_URL)
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    ArgumentCaptor<DeletePolicyRequest> captor = ArgumentCaptor.forClass(
        DeletePolicyRequest.class);
    verify(policyService).deletePolicy(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getDeletedBy()).isEqualTo(USERNAME);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenPolicyNotInDraftState() {
    WrongPolicyStateException exception = new WrongPolicyStateException(POLICY_ID, SAVED);
    doThrow(exception).when(policyService).deletePolicy(any());

    delete(DELETE_URL)
        .contentType(anything())
        .statusCode(UNPROCESSABLE_ENTITY.value())
        .body(containsString(exception.getMessage()));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, QA, USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    delete(DELETE_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
