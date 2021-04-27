package com.silenteight.simulator.dataset.get;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.dataset.domain.DatasetQuery;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    GetDatasetRestController.class,
    GetDatasetRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class GetDatasetRestControllerTest extends BaseRestControllerTest {

  private static final String GET_DATASET_URL = "/v1/datasets/" + EXTERNAL_DATASET_ID.toString();

  @MockBean
  private DatasetQuery datasetQuery;

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_whenDatasetFoundWithoutState() {
    given(datasetQuery.get(EXTERNAL_DATASET_ID)).willReturn(DATASET_DTO);
    get(GET_DATASET_URL)
        .statusCode(OK.value())
        .body("id", is(ID.toString()))
        .body("name", is(RESOURCE_NAME))
        .body("datasetName", is(DATASET_NAME))
        .body("description", is(DESCRIPTION))
        .body("state", is(STATE.toString()))
        .body("alertsCount", is((int) ALERTS_COUNT))
        .body("query", notNullValue())
        .body("createdAt", notNullValue())
        .body("createdBy", is(CREATED_BY));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    get(GET_DATASET_URL).statusCode(FORBIDDEN.value());
  }
}
