package com.silenteight.serp.governance.policy.list;

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
import static com.silenteight.serp.governance.policy.domain.PolicyState.DRAFT;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListPolicyRequestRestController.class, GenericExceptionControllerAdvice.class })
class ListPolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final String NO_FILTER_POLICY_URL = "/v1/policies";
  private static final String SAVED_POLICY_URL = "/v1/policies?state=SAVED";
  private static final String TWO_STATES_POLICY_URL = "/v1/policies?state=SAVED&state=DRAFT";
  private static final String POLICY_NAME = "TEST_POLICY";
  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now().minusHours(1);
  private static final OffsetDateTime UPDATED_AT = OffsetDateTime.now();
  private static final String CREATED_BY = "USER";
  private static final String UPDATED_BY = "USER2";
  private static final UUID FIRST_POLICY_UUID = randomUUID();
  private static final UUID SECOND_POLICY_UUID = randomUUID();
  private static final String FIRST_RESOURCE_NAME = "policies/" + FIRST_POLICY_UUID.toString();
  private static final String SECOND_RESOURCE_NAME = "policies/" + SECOND_POLICY_UUID.toString();
  private static final PolicyDto FIRST_POLICY_DTO =
      makePolicyDto(FIRST_RESOURCE_NAME, SAVED, FIRST_POLICY_UUID);
  private static final PolicyDto SECOND_POLICY_DTO =
      makePolicyDto(SECOND_RESOURCE_NAME, DRAFT, SECOND_POLICY_UUID);

  private static PolicyDto makePolicyDto(String resourceName, PolicyState saved, UUID policyUuid) {
    return PolicyDto.builder()
        .id(policyUuid)
        .name(resourceName)
        .policyName(POLICY_NAME)
        .state(saved)
        .createdAt(CREATED_AT)
        .updatedAt(UPDATED_AT)
        .createdBy(CREATED_BY)
        .updatedBy(UPDATED_BY)
        .build();
  }

  @MockBean
  private ListPoliciesRequestQuery listPolicyRequestQuery;

  @TestWithRole(roles = { APPROVER, QA, QA_ISSUE_MANAGER, AUDITOR, MODEL_TUNER })
  void its200_whenNoPolicies() {
    given(listPolicyRequestQuery.list(of(SAVED))).willReturn(of());

    get(SAVED_POLICY_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(SAVED_POLICY_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { APPROVER, QA, QA_ISSUE_MANAGER, AUDITOR, MODEL_TUNER })
  void its200_whenNoFilterAndPoliciesFound() {
    given(listPolicyRequestQuery.listAll()).willReturn(of(FIRST_POLICY_DTO));

    get(NO_FILTER_POLICY_URL)
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", equalTo(FIRST_POLICY_UUID.toString()))
        .body("[0].name", equalTo(FIRST_RESOURCE_NAME))
        .body("[0].policyName", equalTo(POLICY_NAME))
        .body("[0].state", equalTo(SAVED.name()))
        .body("[0].createdBy", equalTo(CREATED_BY))
        .body("[0].createdAt", notNullValue())
        .body("[0].updatedBy", equalTo(UPDATED_BY))
        .body("[0].updatedAt", notNullValue());
  }

  @TestWithRole(roles = { APPROVER, QA, QA_ISSUE_MANAGER, AUDITOR, MODEL_TUNER })
  void its200_whenPoliciesFound() {
    given(listPolicyRequestQuery.list(of(SAVED))).willReturn(of(FIRST_POLICY_DTO));

    get(SAVED_POLICY_URL)
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", equalTo(FIRST_POLICY_UUID.toString()))
        .body("[0].name", equalTo(FIRST_RESOURCE_NAME))
        .body("[0].policyName", equalTo(POLICY_NAME))
        .body("[0].state", equalTo(SAVED.name()))
        .body("[0].createdBy", equalTo(CREATED_BY))
        .body("[0].createdAt", notNullValue())
        .body("[0].updatedBy", equalTo(UPDATED_BY))
        .body("[0].updatedAt", notNullValue());
  }

  @TestWithRole(roles = { APPROVER, QA, QA_ISSUE_MANAGER, AUDITOR, MODEL_TUNER })
  void its200_whenPoliciesFoundInTwoStates() {
    given(listPolicyRequestQuery.list(of(SAVED, DRAFT))).willReturn(
        of(FIRST_POLICY_DTO, SECOND_POLICY_DTO));

    get(TWO_STATES_POLICY_URL)
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0].id", equalTo(FIRST_POLICY_UUID.toString()))
        .body("[0].name", equalTo(FIRST_RESOURCE_NAME))
        .body("[0].policyName", equalTo(POLICY_NAME))
        .body("[0].state", equalTo(PolicyState.SAVED.name()))
        .body("[0].createdBy", equalTo(CREATED_BY))
        .body("[0].createdAt", notNullValue())
        .body("[0].updatedBy", equalTo(UPDATED_BY))
        .body("[0].updatedAt", notNullValue())
        .body("[1].id", equalTo(SECOND_POLICY_UUID.toString()))
        .body("[1].name", equalTo(SECOND_RESOURCE_NAME))
        .body("[1].policyName", equalTo(POLICY_NAME))
        .body("[1].state", equalTo(PolicyState.DRAFT.name()))
        .body("[1].createdBy", equalTo(CREATED_BY))
        .body("[1].createdAt", notNullValue())
        .body("[1].updatedBy", equalTo(UPDATED_BY))
        .body("[1].updatedAt", notNullValue());
  }
}
