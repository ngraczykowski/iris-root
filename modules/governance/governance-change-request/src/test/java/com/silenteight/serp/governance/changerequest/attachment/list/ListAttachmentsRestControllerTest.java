package com.silenteight.serp.governance.changerequest.attachment.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListAttachmentsRestController.class })
class ListAttachmentsRestControllerTest extends BaseRestControllerTest {

  @MockBean
  ListAttachmentsQuery listAttachmentsQuery;

  private static final UUID CHANGE_REQUEST_ID = randomUUID();
  private static final String ATTACHMENTS_LIST_URL =
      "/v1/changeRequests/" + CHANGE_REQUEST_ID + "/attachments";

  private static final List<String> ATTACHMENTS_ID_LIST = of(
      "files/b8263cd4-a812-46e6-83fd-26f33433ba01",
      "file/ff61e913-8d78-4efd-823a-4e6e117cf4e2",
      "files/b875148d-a9b1-4a71-af75-27a1862ffd7d");


  @TestWithRole(roles = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(ATTACHMENTS_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { AUDITOR, APPROVER, MODEL_TUNER })
  void its200_whenPermittedRole() {
    given(listAttachmentsQuery.list(CHANGE_REQUEST_ID))
        .willReturn(ATTACHMENTS_ID_LIST);

    get(ATTACHMENTS_LIST_URL).statusCode(OK.value())
                             .body("", equalTo(ATTACHMENTS_ID_LIST));

    verify(listAttachmentsQuery).list(CHANGE_REQUEST_ID);
  }
}
