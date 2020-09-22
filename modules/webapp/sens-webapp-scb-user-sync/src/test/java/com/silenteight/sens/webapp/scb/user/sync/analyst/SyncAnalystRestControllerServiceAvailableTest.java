package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.SyncAnalystStatsDtoFixtures.ALL_CHANGED_WITH_ONE_ERROR;
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
        .body("addedRole", is(ALL_CHANGED_WITH_ONE_ERROR.getAddedRole()))
        .body("updatedDisplayName", is(ALL_CHANGED_WITH_ONE_ERROR.getUpdatedDisplayName()))
        .body("deleted", is(ALL_CHANGED_WITH_ONE_ERROR.getDeleted()))
        .body("errors", is(ALL_CHANGED_WITH_ONE_ERROR.getErrors()));
  }

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    post("/users/sync/analysts").statusCode(FORBIDDEN.value());
  }
}
