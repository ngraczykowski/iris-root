package com.silenteight.serp.governance.changerequest.attachment.add;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ AddAttachmentsRestController.class, GenericExceptionControllerAdvice.class })
class AddAttachmentsRestControllerTest extends BaseRestControllerTest {

  @MockBean
  AddAttachmentsUseCase addAttachmentsUseCase;

  private static final UUID CHANGE_REQUEST_ID = randomUUID();
  private static final String ADD_ATTACHMENT_URL =
      "/v1/changeRequests/" + CHANGE_REQUEST_ID + "/attachments";

  private static final List<String> ATTACHMENTS_LIST = of(
      "files/49b0b1e8-49f2-4d6d-9bd6-a23006a8ddee",
      "files/51148b72-e69e-451e-abe7-7c73607535f2",
      "files/123a4e6f-7cdb-40a7-8c65-87e7e8849537"
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

  @ParameterizedTest
  @MethodSource("getIncorrectAttachments")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400WhenAttachmentNotMatchRegexp(List<String> attachments) {
    post(ADD_ATTACHMENT_URL, attachments)
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }

  private static Stream<List<String>> getIncorrectAttachments() {
    return Stream.of(of("files/this-is-no-uuid"), of("name/d293a102-85be-11ec-8c53-5f27abcfa50c"));
  }
}
