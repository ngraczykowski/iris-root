package com.silenteight.simulator.dataset.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.dataset.domain.DatasetQuery;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.domain.AlertMatch.MULTI;
import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
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

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenDatasetsFoundWithoutState() {
    given(datasetQuery.list(new HashSet<>())).willReturn(List.of(DATASET_DTO));
    get("/v1/datasets")
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(ID_1.toString()))
        .body("[0].name", is(RESOURCE_NAME_1))
        .body("[0].datasetName", is(DATASET_NAME))
        .body("[0].description", is(DESCRIPTION))
        .body("[0].state", is(STATE.toString()))
        .body("[0].alertsCount", is((int) ALERTS_COUNT))
        .body("[0].query", notNullValue())
        .body("[0].query.alertGenerationDate.from", is(FROM_AS_STRING))
        .body("[0].query.alertGenerationDate.to", is(TO_AS_STRING))
        .body("[0].query.displayRangeFrom", is(DISPLAY_RANGE_FROM))
        .body("[0].query.displayRangeTo", is(DISPLAY_RANGE_TO))
        .body("[0].alertMatch", is(MULTI.name()))
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(CREATED_BY));
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenDatasetsFoundWitStates() {
    given(datasetQuery.list(Set.of(ACTIVE, ARCHIVED))).willReturn(List.of(DATASET_DTO));
    get("/v1/datasets?state=ACTIVE&state=ARCHIVED")
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(ID_1.toString()))
        .body("[0].name", is(RESOURCE_NAME_1))
        .body("[0].datasetName", is(DATASET_NAME))
        .body("[0].description", is(DESCRIPTION))
        .body("[0].state", is(STATE.toString()))
        .body("[0].alertsCount", is((int) ALERTS_COUNT))
        .body("[0].query", notNullValue())
        .body("[0].alertMatch", is(MULTI.name()))
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(CREATED_BY));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get("/v1/datasets").statusCode(FORBIDDEN.value());
  }
}
