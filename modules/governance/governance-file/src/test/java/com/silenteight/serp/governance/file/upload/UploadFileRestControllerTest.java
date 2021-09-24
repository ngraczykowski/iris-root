package com.silenteight.serp.governance.file.upload;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;
import com.silenteight.serp.governance.file.upload.dto.FileUploadRequestDto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.time.LocalDate.*;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ UploadFileRestController.class })
@Disabled
class UploadFileRestControllerTest extends BaseRestControllerTest {

  private static final String UPLOAD_URL = "/v1/files";

  @MockBean
  UploadFileUseCase uploadFileUseCase;

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
    post(UPLOAD_URL).status(FORBIDDEN);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenInvokedByModelTuner() {
    when(uploadFileUseCase.activate(any(), any())).thenReturn(FILE_REFERENCE_DTO);

    post(UPLOAD_URL, makeAttachmentUploadRequestDto()).status(OK)
        .body("uploaderName", is(USERNAME));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = APPROVER)
  void its200_whenInvokedByApprover() {
    when(uploadFileUseCase.activate(any(), any())).thenReturn(FILE_REFERENCE_DTO);

    post(UPLOAD_URL, makeAttachmentUploadRequestDto()).status(OK)
        .body("uploaderName", is(USERNAME));
  }

  private static FileUploadRequestDto makeAttachmentUploadRequestDto() {
    return new FileUploadRequestDto(null);
  }
}
