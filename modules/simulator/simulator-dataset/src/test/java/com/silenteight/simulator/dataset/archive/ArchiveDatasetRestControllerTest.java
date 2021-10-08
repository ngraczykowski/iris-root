package com.silenteight.simulator.dataset.archive;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.ARCHIVE_DATASET_REQUEST;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.ID;
import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    ArchiveDatasetRestController.class,
    GenericExceptionControllerAdvice.class
})
class ArchiveDatasetRestControllerTest extends BaseRestControllerTest {

  private static final String ARCHIVE_DATASET_URL = format("/v1/datasets/%s:archive", ID);

  @MockBean
  private ArchiveDatasetUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its204_whenDatasetArchived() {
    doNothing().when(useCase).activate(any());

    post(ARCHIVE_DATASET_URL, ARCHIVE_DATASET_REQUEST)
        .statusCode(NO_CONTENT.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, AUDITOR, USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForArchiving() {
    post(ARCHIVE_DATASET_URL, ARCHIVE_DATASET_REQUEST)
        .statusCode(FORBIDDEN.value());
  }
}
