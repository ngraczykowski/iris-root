package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.user.sync.analyst.SyncAnalystService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.user.rest.SyncAnalystStatsDtoFixtures.ALL_CHANGED;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import({ SyncAnalystRestController.class, SyncAnalystRestControllerAdvice.class })
public class SyncAnalystRestControllerServiceAvailableTest extends BaseRestControllerTest {

  @MockBean
  private SyncAnalystService service;

  @Test
  void producesStats_whenAnalystsAreSync() {
    // given
    when(service.synchronize()).thenReturn(ALL_CHANGED);

    // when, then
    post("/users/sync/analysts")
        .statusCode(OK.value())
        .body("added", is(ALL_CHANGED.getAdded()))
        .body("updatedRole", is(ALL_CHANGED.getUpdatedRole()))
        .body("updatedDisplayName", is(ALL_CHANGED.getUpdatedDisplayName()))
        .body("deleted", is(ALL_CHANGED.getDeleted()));
  }
}
