package com.silenteight.sens.webapp.role.edit;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.role.edit.dto.EditRoleDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.stream.Stream;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(EditRoleRestController.class)
class EditRoleRestControllerTest extends BaseRestControllerTest {

  private static final String EDIT_ROLE_URL = "/v2/roles/" + ROLE_ID_1;

  @MockBean private EditRoleUseCase editRoleUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenRoleEdited() {
    doNothing().when(editRoleUseCase).activate(any());

    put(EDIT_ROLE_URL, EDIT_ROLE_DTO).statusCode(OK.value());
  }

  @TestWithRole(roles = {APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER})
  void its403_whenNotPermittedRole() {
    put(EDIT_ROLE_URL, EDIT_ROLE_DTO).contentType(anything()).statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource("getCharsBreakingFieldRegex")
  void its400_whenInvalidName(String invalidPart) {
    EditRoleDto editRoleDto =
        EditRoleDto.builder()
            .name(ROLE_NAME_2 + invalidPart)
            .description(ROLE_DESCRIPTION_2)
            .permissions(PERMISSION_IDS_2)
            .build();
    put(EDIT_ROLE_URL, editRoleDto).statusCode(BAD_REQUEST.value());
  }

  @ParameterizedTest
  @MethodSource("getCharsBreakingFieldRegex")
  void its400_whenInvalidDescription(String invalidPart) {
    EditRoleDto editRoleDto =
        EditRoleDto.builder()
            .name(ROLE_NAME_2)
            .description(ROLE_DESCRIPTION_2 + invalidPart)
            .permissions(PERMISSION_IDS_2)
            .build();
    put(EDIT_ROLE_URL, editRoleDto).statusCode(BAD_REQUEST.value());
  }

  private static Stream<String> getCharsBreakingFieldRegex() {
    return Stream.of("#", "$", "%", ";", "{", ")", "[", ":", ">");
  }
}
