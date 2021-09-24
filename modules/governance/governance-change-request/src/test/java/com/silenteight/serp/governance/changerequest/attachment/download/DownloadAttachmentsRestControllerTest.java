package com.silenteight.serp.governance.changerequest.attachment.download;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.file.storage.FileWrapper;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ DownloadAttachmentsRestController.class })
class DownloadAttachmentsRestControllerTest extends BaseRestControllerTest {

  @MockBean
  DownloadAttachmentsUseCase downloadAttachmentsUseCase;

  private static final String FILE_NAME = "files/aae57e80-a013-41c0-bc12-1582447c26e6";
  private static final String DOWNLOAD_ATTACHMENT_URL =
      "/v1/changeRequest/" + CHANGE_REQUEST_ID + "/attachments/download?file=" + FILE_NAME;

  private static final byte[] FILE_CONTENT = "Test Content".getBytes(UTF_8);
  private static final FileWrapper FILE_WRAPPER = FileWrapper.builder()
      .fileName("test.pdf")
      .mimeType("application/pdf")
      .content(FILE_CONTENT)
      .build();

  @TestWithRole(roles = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(DOWNLOAD_ATTACHMENT_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { APPROVER, MODEL_TUNER,AUDITOR })
  void its200WhenInvokedDownloadAttachment() {
    given(downloadAttachmentsUseCase.activate(FILE_NAME)).willReturn(FILE_WRAPPER);

    final byte[] response = get(DOWNLOAD_ATTACHMENT_URL).statusCode(OK.value())
        .header("Content-Type", "application/pdf")
        .header("Content-Disposition", "attachment; filename=test.pdf")
        .extract().body().asByteArray();

    assertThat(response).isEqualTo(FILE_CONTENT);
  }
}
