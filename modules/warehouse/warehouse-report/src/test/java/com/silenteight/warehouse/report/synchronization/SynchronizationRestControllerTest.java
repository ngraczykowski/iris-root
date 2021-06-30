package com.silenteight.warehouse.report.synchronization;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.synchronization.SynchronizationRestController.REPORT_SYNCHRONIZATION_URL;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    SynchronizationRestController.class,
    GenericExceptionControllerAdvice.class
})
class SynchronizationRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReportSynchronizationUseCase reportSynchronizationUseCase;

  @Disabled("According to security matrix no one have this permission")
  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenSynchronizationSuccessful() {
    doNothing().when(reportSynchronizationUseCase).activate();

    post(REPORT_SYNCHRONIZATION_URL)
        .statusCode(OK.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, USER_ADMINISTRATOR, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreating() {
    post(REPORT_SYNCHRONIZATION_URL)
        .statusCode(FORBIDDEN.value());
  }
}
