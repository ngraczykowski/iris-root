package com.silenteight.serp.governance.policy.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.edit.dto.EditPolicyDto;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ EditPolicyRequestRestController.class,
          EditPolicyConfiguration.class,
          GenericExceptionControllerAdvice.class })
class EditPolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String POLICY_NAME = "policy_name";
  private static final String POLICY_DESCRIPTION = "policy_description";

  private static final String EDIT_POLICY_URL = "/v1/policies/";
  private static final String EDIT_URL = EDIT_POLICY_URL + POLICY_ID;

  @MockBean
  private PolicyService policyService;
  @MockBean
  private UsePolicyEventHandler eventHandler;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenPolicyEdited() {
    patch(EDIT_URL, getEditPolicyDto())
        .contentType(anything())
        .statusCode(OK.value());

    verify(policyService).updatePolicy(
        POLICY_ID, POLICY_NAME, POLICY_DESCRIPTION, USERNAME);
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    patch(EDIT_URL, getEditPolicyDto())
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @NotNull
  private EditPolicyDto getEditPolicyDto() {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setName(POLICY_NAME);
    editPolicyDto.setDescription(POLICY_DESCRIPTION);
    return editPolicyDto;
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenSavedStatusChanged() {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setState(PolicyState.SAVED);

    patch(EDIT_URL, editPolicyDto)
        .contentType(anything())
        .statusCode(OK.value());

    verify(policyService).savePolicy(POLICY_ID, USERNAME);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenUseStatusChanged() {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setState(PolicyState.IN_USE);
    ArgumentCaptor<PolicyPromotedEvent> eventHandled = ArgumentCaptor
        .forClass(PolicyPromotedEvent.class);

    patch(EDIT_URL, editPolicyDto)
        .contentType(anything())
        .statusCode(OK.value());

    verify(policyService).usePolicy(POLICY_ID, USERNAME);
    verify(eventHandler).handle(eventHandled.capture());
    assertThat(eventHandled.getValue().getPolicyId()).isEqualTo(POLICY_ID);
  }

  static class UsePolicyEventHandler {
    @EventListener
    public void handle(PolicyPromotedEvent event) {
    }
  }
}
