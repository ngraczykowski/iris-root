package com.silenteight.sens.webapp.sso.create;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.sso.create.dto.CreateSsoMappingDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.stream.Stream;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ CreateSsoMappingRestController.class })
class CreateSsoMappingRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_SSO_MAPPING_URL = "/sso/mappings";

  @MockBean
  CreateSsoMappingUseCase createSsoMappingUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its201WhenSsoMappingIsCreated() {
    doNothing().when(createSsoMappingUseCase).activate(any());

    post(CREATE_SSO_MAPPING_URL, CREATE_SSO_MAPPING_DTO).statusCode(CREATED.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(CREATE_SSO_MAPPING_URL, CREATE_SSO_MAPPING_DTO)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource("getDtosWithInvalidFieldsLength")
  void its400WhenFieldLengthIsNotValid(CreateSsoMappingDto createRoleDto) {
    doNothing().when(createSsoMappingUseCase).activate(any());

    post(CREATE_SSO_MAPPING_URL, createRoleDto)
        .statusCode(BAD_REQUEST.value());
  }

  private static Stream<CreateSsoMappingDto> getDtosWithInvalidFieldsLength() {
    return Stream.of(
        CREATE_SSO_MAPPING_DTO_WITH_TOO_LONG_NAME,
        CREATE_SSO_MAPPING_DTO_WITH_TOO_SHORT_NAME,
        CREATE_SSO_MAPPING_DTO_WITH_ATTRIBUTE_NAME_TOO_LONG,
        CREATE_SSO_MAPPING_DTO_WITH_ATTRIBUTE_NAME_TOO_SHORT,
        CREATE_SSO_MAPPING_DTO_WITH_ROLE_NAME_TOO_LONG,
        CREATE_SSO_MAPPING_DTO_WITH_ROLE_NAME_TOO_SHORT);
  }
}
