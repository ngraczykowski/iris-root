package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.scb.user.sync.analyst.SyncAnalystRestController;
import com.silenteight.sens.webapp.scb.user.sync.analyst.SyncAnalystRestControllerAdvice;

import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ADMIN;
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
