package com.silenteight.sens.webapp.backend.decisiontree.details;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    DecisionTreeDetailsRestController.class,
    DecisionTreeDetailsRestControllerAdvice.class })
class DecisionTreeDetailsRestControllerTest extends BaseRestControllerTest {

  private static final long DECISION_TREE_ID = 123;
  private static final long OTHER_DECISION_TREE_ID = 456;
  @MockBean
  private DecisionTreeQuery decisionTreeQuery;

  @TestWithRole(roles = { ADMINISTRATOR, APPROVER, AUDITOR, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    given(decisionTreeQuery.getDecisionTree(DECISION_TREE_ID)).willReturn(
        new DecisionTreeDto(DECISION_TREE_ID));

    get("/decision-trees/" + DECISION_TREE_ID)
        .statusCode(OK.value())
        .body("id", is((int) DECISION_TREE_ID));
  }

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its404_whenNotFound() {
    given(decisionTreeQuery.getDecisionTree(OTHER_DECISION_TREE_ID))
        .willThrow(DecisionTreeNotFound.of(OTHER_DECISION_TREE_ID));

    get("/decision-trees/" + OTHER_DECISION_TREE_ID)
        .statusCode(NOT_FOUND.value())
        .body("key", equalTo("DecisionTreeNotFound"))
        .body("extras.treeId", equalTo((int) OTHER_DECISION_TREE_ID));
  }

  @TestWithRole(roles = { ANALYST })
  void its403_whenNotPermittedRole() {
    get("/decision-trees/" + DECISION_TREE_ID)
        .statusCode(FORBIDDEN.value());
  }
}
