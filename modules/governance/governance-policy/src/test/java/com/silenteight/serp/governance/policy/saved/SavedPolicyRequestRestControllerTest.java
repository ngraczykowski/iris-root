package com.silenteight.serp.governance.policy.saved;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ SavedPolicyRequestRestController.class, GenericExceptionControllerAdvice.class })
class SavedPolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final String SAVED_POLICY_URL = "/v1/policies?state=SAVED";
  private static final String POLICY_NAME = "TEST_POLICY";
  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now().minusHours(1);
  private static final OffsetDateTime UPDATED_AT = OffsetDateTime.now();
  private static final String CREATED_BY = "USER";
  private static final String UPDATED_BY = "USER2";
  private static final UUID FIRST_POLICY_UUID = UUID.randomUUID();
  private static final UUID SECOND_POLICY_UUID = UUID.randomUUID();
  private static final PolicyDto FIRST_POLICY_DTO = getPolicyDto(
      1, PolicyState.SAVED, FIRST_POLICY_UUID);
  private static final PolicyDto SECOND_POLICY_DTO =
      getPolicyDto(2, PolicyState.DRAFT, SECOND_POLICY_UUID);

  private static PolicyDto getPolicyDto(int id, PolicyState saved, UUID policyUuid) {
    return PolicyDto
        .builder()
        .id(id)
        .name(POLICY_NAME)
        .state(saved)
        .policyId(policyUuid)
        .createdAt(CREATED_AT)
        .updatedAt(UPDATED_AT)
        .createdBy(CREATED_BY)
        .updatedBy(UPDATED_BY)
        .build();
  }

  @MockBean
  private SavedPolicyRequestQuery savedPolicyRequestQuery;

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenNoPolicies() {
    given(savedPolicyRequestQuery.listSaved()).willReturn(of());

    get(SAVED_POLICY_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(SAVED_POLICY_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenPoliciesFound() {
    given(savedPolicyRequestQuery.listSaved()).willReturn(
        asList(FIRST_POLICY_DTO, SECOND_POLICY_DTO));

    get(SAVED_POLICY_URL)
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0].id", equalTo(1))
        .body("[0].policyId", equalTo(FIRST_POLICY_UUID.toString()))
        .body("[0].state", equalTo(PolicyState.SAVED.name()))
        .body("[0].createdBy", equalTo(CREATED_BY))
        .body("[0].createdAt", notNullValue())
        .body("[0].updatedBy", equalTo(UPDATED_BY))
        .body("[0].updatedAt", notNullValue())
        .body("[1].id", equalTo(2))
        .body("[1].policyId", equalTo(SECOND_POLICY_UUID.toString()))
        .body("[1].state", equalTo(PolicyState.DRAFT.name()))
        .body("[1].createdBy", equalTo(CREATED_BY))
        .body("[1].createdAt", notNullValue())
        .body("[1].updatedBy", equalTo(UPDATED_BY))
        .body("[1].updatedAt", notNullValue());
  }
}
