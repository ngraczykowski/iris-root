package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.user.sync.analyst.SyncAnalystsUseCase;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.backend.user.rest.SyncAnalystStatsDtoFixtures.ALL_CHANGED_WITH_ONE_ERROR;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ SyncAnalystRestController.class, SyncAnalystRestControllerAdvice.class })
class SyncAnalystRestControllerServiceAvailableTest extends BaseRestControllerTest {

  @MockBean
  private SyncAnalystsUseCase service;

  @TestWithRole(role = ADMIN)
  void producesStats_whenAnalystsAreSync() {
    // given
    when(service.synchronize()).thenReturn(ALL_CHANGED_WITH_ONE_ERROR);

    // when, then
    post("/users/sync/analysts")
        .statusCode(OK.value())
        .body("added", is(ALL_CHANGED_WITH_ONE_ERROR.getAdded()))
        .body("restored", is(ALL_CHANGED_WITH_ONE_ERROR.getRestored()))
        .body("addedRole", is(ALL_CHANGED_WITH_ONE_ERROR.getAddedRole()))
        .body("updatedDisplayName", is(ALL_CHANGED_WITH_ONE_ERROR.getUpdatedDisplayName()))
        .body("deleted", is(ALL_CHANGED_WITH_ONE_ERROR.getDeleted()))
        .body("errors", is(ALL_CHANGED_WITH_ONE_ERROR.getErrors()));
  }

  @TestWithRole(roles = { ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    post("/users/sync/analysts").statusCode(FORBIDDEN.value());
  }

  @TestWithRole(role = ADMIN)
  void registersAuditLogMessage() {
    // given
    when(service.synchronize()).thenReturn(ALL_CHANGED_WITH_ONE_ERROR);

    // when
    post("/users/sync/analysts");

    // then
    verify(auditLog).logInfo(USER_MANAGEMENT, "Synchronizing Analysts", new Object[0]);
  }
}
