package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Import({ SyncAnalystRestController.class, SyncAnalystRestControllerAdvice.class })
class SyncAnalystRestControllerServiceUnavailableTest extends BaseRestControllerTest  {

  @Test
  void its503_whenSyncAnalystsUseCaseNotAvailable() {
    // when, then
    post("/users/sync/analysts")
        .statusCode(SERVICE_UNAVAILABLE.value())
        .body(is("Analyst synchronization not available."));
  }
}
