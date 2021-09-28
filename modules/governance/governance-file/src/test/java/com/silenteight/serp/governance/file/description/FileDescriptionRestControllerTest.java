package com.silenteight.serp.governance.file.description;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.time.LocalDate.now;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ FileDescriptionRestController.class })
class FileDescriptionRestControllerTest extends BaseRestControllerTest {

  @MockBean
  FileDescriptionQuery fileDescriptionQuery;

  private static final String GET_DESCRIPTION_URL =
      "/v1/files/7973eb42-25e6-40a5-b21f-c43deefddc0d/description";

  private static final LocalDate UPLOAD_DATE = now();
  private static final String FILE_ID = "7973eb42-25e6-40a5-b21f-c43deefddc0d";
  private static final String FILE_NAME = "test.pdf";
  private static final String UPLOADER_NAME = "John Doe";
  private static final FileReferenceDto FILED_DESCRIPTION_DTO = FileReferenceDto.builder()
      .uploaderName(UPLOADER_NAME)
      .uploadDate(UPLOAD_DATE)
      .fileSize(1236L)
      .fileId(fromString(FILE_ID))
      .fileName(FILE_NAME)
      .mimeType("application/text")
      .build();

  @TestWithRole(roles = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(GET_DESCRIPTION_URL)
        .status(FORBIDDEN);
  }

  @TestWithRole(roles = { MODEL_TUNER, AUDITOR, APPROVER })
  void its200_whenPermittedRole() {
    Mockito
        .when(fileDescriptionQuery.get("7973eb42-25e6-40a5-b21f-c43deefddc0d"))
        .thenReturn(FILED_DESCRIPTION_DTO);

    get(GET_DESCRIPTION_URL).status(OK)
        .body("fileName", is(FILE_NAME))
        .body("uploaderName", is(UPLOADER_NAME));
  }
}
