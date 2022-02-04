package com.silenteight.serp.governance.policy.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.ArchivePolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.SavePolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.UpdatePolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.UsePolicyRequest;
import com.silenteight.serp.governance.policy.domain.events.NewPolicyInUseEvent;
import com.silenteight.serp.governance.policy.edit.dto.EditPolicyDto;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.QA;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.POLICY_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ EditPolicyRequestRestController.class,
          EditPolicyConfiguration.class,
          GenericExceptionControllerAdvice.class })
class EditPolicyRequestRestControllerTest extends BaseRestControllerTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String POLICY_DESCRIPTION = "policy_description";

  private static final String EDIT_POLICY_URL = "/v1/policies/";
  private static final String EDIT_URL = EDIT_POLICY_URL + POLICY_ID;

  @MockBean
  private PolicyService policyService;
  @MockBean
  private UsePolicyEventHandler eventHandler;

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.TestFixtures#getPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenPolicyEdited(String policyName) {
    patch(EDIT_URL, getEditPolicyDto(policyName))
        .contentType(anything())
        .statusCode(OK.value());

    ArgumentCaptor<UpdatePolicyRequest> captor = ArgumentCaptor.forClass(UpdatePolicyRequest.class);
    verify(policyService).updatePolicy(captor.capture());
    assertThat(captor.getValue().getId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getPolicyName()).isEqualTo(policyName);
    assertThat(captor.getValue().getDescription()).isEqualTo(POLICY_DESCRIPTION);
    assertThat(captor.getValue().getUpdatedBy()).isEqualTo(USERNAME);
  }

  @TestWithRole(roles = { APPROVER, QA, USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    patch(EDIT_URL, getEditPolicyDto(POLICY_NAME))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @NotNull
  private EditPolicyDto getEditPolicyDto(String policyName) {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setPolicyName(policyName);
    editPolicyDto.setDescription(POLICY_DESCRIPTION);
    return editPolicyDto;
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenSavedStatusChanged() {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setState(PolicyState.SAVED);

    patch(EDIT_URL, editPolicyDto)
        .contentType(anything())
        .statusCode(OK.value());

    ArgumentCaptor<SavePolicyRequest> captor = ArgumentCaptor.forClass(SavePolicyRequest.class);
    verify(policyService).savePolicy(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getSavedBy()).isEqualTo(USERNAME);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenArchivedStatusChanged() {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setState(PolicyState.ARCHIVED);

    patch(EDIT_URL, editPolicyDto)
        .contentType(anything())
        .statusCode(OK.value());

    ArgumentCaptor<ArchivePolicyRequest> captor = ArgumentCaptor.forClass(
        ArchivePolicyRequest.class);
    verify(policyService).archivePolicy(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getArchivedBy()).isEqualTo(USERNAME);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenUseStatusChanged() {
    EditPolicyDto editPolicyDto = new EditPolicyDto();
    editPolicyDto.setState(PolicyState.IN_USE);
    ArgumentCaptor<NewPolicyInUseEvent> eventHandled = ArgumentCaptor
        .forClass(NewPolicyInUseEvent.class);

    patch(EDIT_URL, editPolicyDto)
        .contentType(anything())
        .statusCode(OK.value());

    ArgumentCaptor<UsePolicyRequest> captor = ArgumentCaptor.forClass(UsePolicyRequest.class);
    verify(policyService).usePolicy(captor.capture());
    assertThat(captor.getValue().getPolicyId()).isEqualTo(POLICY_ID);
    assertThat(captor.getValue().getActivatedBy()).isEqualTo(USERNAME);
    verify(eventHandler).handle(eventHandled.capture());
    assertThat(eventHandled.getValue().getPolicyId()).isEqualTo(POLICY_ID);
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.TestFixtures#getIncorrectPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenPolicyNameLengthIsWrong(String policyName) {
    patch(EDIT_URL, getEditPolicyDto(policyName))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }

  static class UsePolicyEventHandler {

    @EventListener
    public void handle(NewPolicyInUseEvent event) {
    }
  }
}
