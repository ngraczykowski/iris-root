package com.silenteight.simulator.dataset.get;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.dataset.domain.exception.DatasetNotFoundException;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    GetDatasetRestController.class,
    GetDatasetControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class GetDatasetRestControllerTest extends BaseRestControllerTest {

  private static final String GET_DATASET_URL = "/v1/datasets/" + ID_1;

  @MockBean
  private DatasetQuery datasetQuery;

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenDatasetFound() {
    given(datasetQuery.get(ID_1)).willReturn(DATASET_DTO);
    get(GET_DATASET_URL)
        .statusCode(OK.value())
        .body("id", is(ID_1.toString()))
        .body("name", is(RESOURCE_NAME))
        .body("datasetName", is(DATASET_NAME))
        .body("description", is(DESCRIPTION))
        .body("state", is(STATE.toString()))
        .body("alertsCount", is((int) ALERTS_COUNT))
        .body("query", notNullValue())
        .body("createdAt", notNullValue())
        .body("createdBy", is(CREATED_BY));
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its404_whenDatasetNotFound() {
    given(datasetQuery.get(ID_1)).willThrow(DatasetNotFoundException.class);
    get(GET_DATASET_URL)
        .statusCode(NOT_FOUND.value());
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(GET_DATASET_URL).statusCode(FORBIDDEN.value());
  }
}
