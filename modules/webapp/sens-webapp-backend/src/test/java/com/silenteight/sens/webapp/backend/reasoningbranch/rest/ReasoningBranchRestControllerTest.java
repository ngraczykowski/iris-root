package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.AI_SOLUTION;
import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.BRANCH_ID;
import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.IS_ACTIVE;
import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.TREE_ID;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({ ReasoningBranchRestController.class })
class ReasoningBranchRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @Test
  void its404_whenNonExistingReasoningBranch() {
    given(reasoningBranchDetailsQuery.findByTreeIdAndBranchId(TREE_ID, BRANCH_ID))
        .willReturn(empty());

    get(endpointFor(TREE_ID, BRANCH_ID))
        .contentType(anything())
        .statusCode(NOT_FOUND.value());
  }

  private static String endpointFor(long treeId, long branchId) {
    return String.format("/decision-trees/%s/branches/%s", treeId, branchId);
  }

  @Test
  void its200WithCorrectBody_whenFound() {
    given(reasoningBranchDetailsQuery.findByTreeIdAndBranchId(TREE_ID, BRANCH_ID))
        .willReturn(of(new BranchDetailsDto(BRANCH_ID, AI_SOLUTION, IS_ACTIVE)));

    get(endpointFor(TREE_ID, BRANCH_ID))
        .statusCode(OK.value())
        .body("reasoningBranchId", equalTo((int) BRANCH_ID))
        .body("aiSolution", equalTo(AI_SOLUTION))
        .body("active", equalTo(IS_ACTIVE));
  }

  static class ReasoningBranchRestControllerFixtures {

    static final long BRANCH_ID = 5;
    static final long TREE_ID = 2;

    static final String AI_SOLUTION = "True Positive";
    static final boolean IS_ACTIVE = false;
  }
}
