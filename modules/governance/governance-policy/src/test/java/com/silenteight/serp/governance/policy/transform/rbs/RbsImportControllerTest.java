package com.silenteight.serp.governance.policy.transform.rbs;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.File;
import java.net.URISyntaxException;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.policy.transform.rbs.RbsImportController.IMPORT_FROM_RBS_URL;
import static com.silenteight.serp.governance.policy.transform.rbs.RbsImportFixture.IMPORT_FILE_NAME;
import static com.silenteight.serp.governance.policy.transform.rbs.RbsImportFixture.POLICY_UUID;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    RbsImportController.class,
    RbsImportControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class RbsImportControllerTest extends BaseRestControllerTest {

  @MockBean
  private RbsImportUseCase rbsImportUseCase;

  static File importFile;

  @BeforeAll
  static void setup() throws URISyntaxException {
    importFile = new File(RbsImportFixture.IMPORT_FILE_URL.toURI());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenRbsImport() {
    var importCommandArgumentCaptor = ArgumentCaptor.forClass(RbsImportCommand.class);
    when(rbsImportUseCase.apply(importCommandArgumentCaptor.capture()))
        .thenReturn(POLICY_UUID);

    postAsync(IMPORT_FROM_RBS_URL, importFile)
        .statusCode(ACCEPTED.value());

    assertNotNull(importCommandArgumentCaptor.getValue().getInputStream());
    assertEquals(IMPORT_FILE_NAME, importCommandArgumentCaptor.getValue().getFileName());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenFileIsNotValid() {
    when(rbsImportUseCase.apply(any()))
        .thenThrow(RbsParserException.class);

    postAsync(IMPORT_FROM_RBS_URL, importFile)
        .statusCode(BAD_REQUEST.value());
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, QA, USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    post(IMPORT_FROM_RBS_URL, importFile)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
