package com.silenteight.serp.governance.policy.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.details.dto.PolicyDetailsDto;
import com.silenteight.serp.governance.policy.domain.PolicyState;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({ PolicyDetailsRequestRestController.class, GenericExceptionControllerAdvice.class })
class PolicyDetailsRequestRestControllerTest extends BaseRestControllerTest {

  private static final String POLICY_URL = "/v1/policies/";
  private static final String POLICY_NAME = "TEST_POLICY";
  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now().minusHours(1);
  private static final OffsetDateTime UPDATED_AT = OffsetDateTime.now();
  private static final String CREATED_BY = "USER";
  private static final String UPDATED_BY = "USER2";
  private static final UUID POLICY_UUID = UUID.randomUUID();
  private static final int STEPS_COUNT = 2;
  private static final PolicyDetailsDto POLICY_DTO = PolicyDetailsDto
      .builder()
      .policyId(1)
      .name(POLICY_NAME)
      .state(PolicyState.SAVED)
      .id(POLICY_UUID)
      .createdAt(CREATED_AT)
      .updatedAt(UPDATED_AT)
      .createdBy(CREATED_BY)
      .updatedBy(UPDATED_BY)
      .stepsCount(STEPS_COUNT)
      .build();

  @MockBean
  private PolicyDetailsUseCase policyDetailsUseCase;

  @TestWithRole(roles = { POLICY_MANAGER })
  void its404_whenNoPolicies() {
    given(policyDetailsUseCase.activate(POLICY_UUID))
        .willThrow(new EntityNotFoundException());

    get(getPolicyMapper(POLICY_UUID))
        .contentType(anything())
        .statusCode(NOT_FOUND.value());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(getPolicyMapper(POLICY_UUID)).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenPolicyDetails() {
    given(policyDetailsUseCase.activate(POLICY_UUID)).willReturn(POLICY_DTO);

    get(getPolicyMapper(POLICY_UUID))
        .statusCode(OK.value())
        .body("id", equalTo(POLICY_UUID.toString()))
        .body("policyId", equalTo(1))
        .body("state", equalTo(SAVED.name()))
        .body("createdBy", equalTo(CREATED_BY))
        .body("createdAt", notNullValue())
        .body("updatedBy", equalTo(UPDATED_BY))
        .body("updatedAt", notNullValue())
        .body("stepsCount", equalTo(STEPS_COUNT));
  }

  private String getPolicyMapper(UUID policyUuid) {
    return POLICY_URL + policyUuid.toString();
  }
}
