package com.silenteight.serp.governance.policy.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.create.dto.CreatePolicyDto;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import io.restassured.http.ContentType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.domain.PolicyState.DRAFT;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.POLICY_NAME;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreatePolicyRequestRestController.class,
    CreatePolicyConfiguration.class,
    GenericExceptionControllerAdvice.class })
class CreatePolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_POLICY_URL = "/v1/policies";

  private static final UUID POLICY_ID = UUID.randomUUID();

  public static final OffsetDateTime CREATED_AT = OffsetDateTime.now();

  private PolicyDto makePolicyDto(String policyName, String name) {
    return PolicyDto.builder()
        .id(POLICY_ID)
        .policyName(policyName)
        .name(name)
        .state(DRAFT)
        .createdAt(CREATED_AT)
        .updatedAt(CREATED_AT)
        .createdBy(USERNAME)
        .updatedBy(USERNAME)
        .build();
  }

  @MockBean
  private PolicyService policyService;

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.SharedTestFixtures#getPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its201_whenPolicyAdded(String policyName) {
    String name = policyName + "_name";
    PolicyDto createdPolicyDto = makePolicyDto(policyName, name);
    when(policyService.createPolicy(any(), any(), any())).thenReturn(createdPolicyDto);

    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, policyName, DRAFT))
        .contentType(ContentType.JSON)
        .statusCode(CREATED.value())
        .body("id", is(POLICY_ID.toString()))
        .body("policyName", is(policyName))
        .body("name", is(name))
        .body("state", is(DRAFT.toString()))
        .body("createdAt", notNullValue())
        .body("updatedAt", notNullValue())
        .body("createdBy", is(USERNAME))
        .body("updatedBy", is(USERNAME));

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
      "com.silenteight.serp.governance.policy.domain.SharedTestFixtures#getIncorrectPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenPolicyNameLengthIsWrong(String policyName) {
    post(CREATE_POLICY_URL, new CreatePolicyDto(POLICY_ID, policyName, DRAFT))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }
}
