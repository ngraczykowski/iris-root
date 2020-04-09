package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;

import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Import({ SyncAnalystRestController.class, SyncAnalystRestControllerAdvice.class })
class SyncAnalystRestControllerServiceUnavailableTest extends BaseRestControllerTest {

  @TestWithRole(role = ADMIN)
  void its503_whenSyncAnalystsUseCaseNotAvailable() {
    // when, then
    post("/users/sync/analysts")
        .statusCode(SERVICE_UNAVAILABLE.value())
        .body(is("Analyst synchronization not available."));
  }
}
