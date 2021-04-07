package com.silenteight.simulator.dataset.list;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.dataset.domain.DatasetQuery;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ListDatasetRestController.class,
    GenericExceptionControllerAdvice.class
})
class ListDatasetRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private DatasetQuery datasetQuery;

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_whenDatasetsFoundWithoutState() {
    given(datasetQuery.list(null)).willReturn(of(DATASET_DTO));
    get("/v1/datasets")
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(DATASET_ID.toString()))
        .body("[0].name", is(NAME))
        .body("[0].description", is(DESCRIPTION))
        .body("[0].state", is(STATE.toString()))
        .body("[0].alertsCount", is((int) ALERTS_COUNT))
        .body("[0].query", notNullValue())
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(CREATED_BY));
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_whenDatasetsFoundWitState() {
    given(datasetQuery.list(CURRENT)).willReturn(of(DATASET_DTO));
    get("/v1/datasets?state=CURRENT")
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(DATASET_ID.toString()))
        .body("[0].name", is(NAME))
        .body("[0].description", is(DESCRIPTION))
        .body("[0].state", is(STATE.toString()))
        .body("[0].alertsCount", is((int) ALERTS_COUNT))
        .body("[0].query", notNullValue())
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(CREATED_BY));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    get("/v1/datasets").statusCode(FORBIDDEN.value());
  }
}
