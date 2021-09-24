package com.silenteight.serp.governance.changerequest.attachment.delete;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ DeleteAttachmentsRestController.class })
class DeleteAttachmentsRestControllerTest extends BaseRestControllerTest {

  @MockBean
  DeleteAttachmentsUseCase deleteAttachmentsUseCase;

  private static final String FILE_NAME = "files/aae57e80-a013-41c0-bc12-1582447c26e6";
  private static final UUID CHANGE_REQUEST_ID = randomUUID();
  private static final String DELETE_ATTACHMENT_URL =
      "/v1/changeRequests/" + CHANGE_REQUEST_ID + "/attachments?file=" + FILE_NAME;

  @TestWithRole(roles = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    delete(DELETE_ATTACHMENT_URL).contentType(anything()).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { APPROVER, MODEL_TUNER })
  void it200whenDeleteAttachmentInvoked() {
    delete(DELETE_ATTACHMENT_URL)
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(deleteAttachmentsUseCase).deleteAttachments(CHANGE_REQUEST_ID, FILE_NAME);
  }
}
