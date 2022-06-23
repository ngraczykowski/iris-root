package com.silenteight.serp.governance.changerequest.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.create.dto.CreateChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CHANGE_REQUEST_ID;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.CREATOR_COMMENT;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@Import({
    CreateChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class CreateChangeRequestRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_CHANGE_REQUEST_URL = "/v1/changeRequests";

  @MockBean
  private CreateChangeRequestUseCase createChangeRequestUseCase;

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures#getModelNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenChangeRequestCreated(String modelName) {
    post(CREATE_CHANGE_REQUEST_URL, makeCreateChangeRequestDtoWithModelName(modelName))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(createChangeRequestUseCase).activate(
        CreateChangeRequestCommand
            .builder()
            .id(CHANGE_REQUEST_ID)
            .modelName(modelName)
            .createdBy(USERNAME)
            .creatorComment(CREATOR_COMMENT)
            .build());
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(CREATE_CHANGE_REQUEST_URL, makeCreateChangeRequestDtoWithModelName(MODEL_NAME))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures#"
          + "getIncorrectModelNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenFeatureNameLengthIsWrong(String modelName) {
    post(CREATE_CHANGE_REQUEST_URL, makeCreateChangeRequestDtoWithModelName(modelName))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures#"
          + "getForbiddenCharsAsInput")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenFeatureCommentContainsForbiddenChars(String comment) {
    post(CREATE_CHANGE_REQUEST_URL, makeCreateChangeRequestDtoWithCreatorComment(comment))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures#"
          + "getAllowedCharsAsInput")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its200_whenFeatureCommentContainsAllowedChars(String comment) {
    post(CREATE_CHANGE_REQUEST_URL, makeCreateChangeRequestDtoWithCreatorComment(comment))
        .contentType(anything())
        .statusCode(ACCEPTED.value());
  }

  private static CreateChangeRequestDto makeCreateChangeRequestDtoWithModelName(String modelName) {
    return new CreateChangeRequestDto(CHANGE_REQUEST_ID, modelName, CREATOR_COMMENT);
  }

  private static CreateChangeRequestDto makeCreateChangeRequestDtoWithCreatorComment(String comment) {
    return new CreateChangeRequestDto(CHANGE_REQUEST_ID, MODEL_NAME, comment);
  }
}
