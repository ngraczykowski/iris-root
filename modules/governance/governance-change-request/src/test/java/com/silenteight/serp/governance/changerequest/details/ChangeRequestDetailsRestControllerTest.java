package com.silenteight.serp.governance.changerequest.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.domain.ChangeRequestState;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ChangeRequestDetailsRestController.class,
    GenericExceptionControllerAdvice.class
})
class ChangeRequestDetailsRestControllerTest extends BaseRestControllerTest {

  private final Fixtures fixtures = new Fixtures();

  @MockBean
  private ChangeRequestDetailsQuery changeRequestDetailsQuery;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER })
  void its200WithCorrectBody_whenFound() {
    UUID changeRequestId = fixtures.changeRequest.getId();
    given(changeRequestDetailsQuery.details(changeRequestId)).willReturn(fixtures.changeRequest);

    get(mappingForDetails(changeRequestId))
        .statusCode(OK.value())
        .body("id", equalTo("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .body("createdBy", equalTo("Model Tuner #1"))
        .body("createdAt", notNullValue())
        .body("creatorComment", equalTo("Increase efficiency by 20% on Asia markets"))
        .body("decidedBy", equalTo("Approver 1"))
        .body("deciderComment", equalTo("All good!"))
        .body("decidedAt", notNullValue())
        .body("state", equalTo("APPROVED"))
        .body("modelName", equalTo(MODEL_NAME));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(mappingForDetails(fixtures.changeRequest.getId())).statusCode(FORBIDDEN.value());
  }

  private String mappingForDetails(UUID changeRequestId) {
    return "/v1/changeRequests/" + changeRequestId.toString();
  }

  private class Fixtures {

    ChangeRequestDto changeRequest = ChangeRequestDto.builder()
        .id(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .createdBy("Model Tuner #1")
        .createdAt(parse("2020-04-15T10:15:30+01:00", ISO_OFFSET_DATE_TIME))
        .creatorComment("Increase efficiency by 20% on Asia markets")
        .decidedBy("Approver 1")
        .deciderComment("All good!")
        .decidedAt(parse("2020-04-15T10:16:40+01:00", ISO_OFFSET_DATE_TIME))
        .state(ChangeRequestState.APPROVED)
        .modelName(MODEL_NAME)
        .build();
  }
}
