package com.silenteight.serp.governance.file.upload;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static com.google.common.io.Resources.getResource;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ UploadFileRestController.class })
class UploadFileRestControllerTest extends BaseRestControllerTest {

  private static final String UPLOAD_URL = "/v1/files";

  @MockBean
  UploadFileUseCase uploadFileUseCase;

  static File uploadFile;

  static final URL UPLOAD_FILE_URL = getResource(
      "files/AI Reasoning last 24h_2021-09-17T10_10_50.943Z"
          + "_891f94f0-179f-52ec-a00e-6b25ef5112c0.csv");

  @BeforeAll
  static void setup() throws URISyntaxException {
    uploadFile = new File(UPLOAD_FILE_URL.toURI());
  }

  private static final FileReferenceDto FILE_REFERENCE_DTO =
      FileReferenceDto.builder()
          .uploadDate(now())
          .fileId(randomUUID())
          .fileName("report.csv")
          .fileSize(1234L)
          .uploaderName(USERNAME)
          .mimeType("application/text")
          .build();

  @TestWithRole(roles = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(UPLOAD_URL, uploadFile).status(FORBIDDEN);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenInvokedByModelTuner() {
    when(uploadFileUseCase.activate(any(), any())).thenReturn(FILE_REFERENCE_DTO);

    post(UPLOAD_URL, uploadFile).status(OK)
        .body("uploaderName", is(USERNAME));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its200_whenInvokedByApprover() {
    when(uploadFileUseCase.activate(any(), any())).thenReturn(FILE_REFERENCE_DTO);

    post(UPLOAD_URL, uploadFile).status(OK)
        .body("uploaderName", is(USERNAME));
  }
}
