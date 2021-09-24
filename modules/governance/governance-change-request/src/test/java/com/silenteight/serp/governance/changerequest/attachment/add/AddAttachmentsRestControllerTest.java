package com.silenteight.serp.governance.changerequest.attachment.add;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ AddAttachmentsRestController.class })
class AddAttachmentsRestControllerTest extends BaseRestControllerTest {

  @MockBean
  AddAttachmentsUseCase addAttachmentsUseCase;

  private static final UUID CHANGE_REQUEST_ID = randomUUID();
  private static final String ADD_ATTACHMENT_URL =
      "/v1/changeRequests/" + CHANGE_REQUEST_ID + "/attachments";

  private static final List<String> ATTACHMENTS_LIST = of(
      "file/49b0b1e8-49f2-4d6d-9bd6-a23006a8ddee",
      "file/51148b72-e69e-451e-abe7-7c73607535f2",
      "file/123a4e6f-7cdb-40a7-8c65-87e7e8849537"
  );

  @TestWithRole(roles = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(ADD_ATTACHMENT_URL, ATTACHMENTS_LIST)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { APPROVER, MODEL_TUNER })
  void its200WhenAddAttachmentsInvoked() {
    post(ADD_ATTACHMENT_URL, ATTACHMENTS_LIST)
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(addAttachmentsUseCase).addAttachments(CHANGE_REQUEST_ID, ATTACHMENTS_LIST);
  }
}
