package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchesCommand;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCase;
import com.silenteight.sens.webapp.backend.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;

import io.vavr.control.Try;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.*;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.BUSINESS_OPERATOR;
import static io.vavr.control.Try.failure;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;

@Import({ ReasoningBranchRestController.class })
class ReasoningBranchRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @MockBean
  private ReasoningBranchesQuery reasoningBranchesQuery;

  @MockBean
  private UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase;

  private static String mappingForBranch(long treeId, long branchNo) {
    return format("/decision-trees/%s/branches/%s", treeId, branchNo);
  }

  private static String mappingForBranches(long treeId) {
    return format("/decision-trees/%s/branches", treeId);
  }

  @Nested
  class BranchDetails {

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its404_whenNonExistingReasoningBranch() {
      given(reasoningBranchDetailsQuery.findByTreeIdAndBranchId(TREE_ID, BRANCH_NO_1))
          .willReturn(empty());

      get(mappingForBranch(TREE_ID, BRANCH_NO_1))
          .contentType(anything())
          .statusCode(NOT_FOUND.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200WithCorrectBody_whenFound() {
      given(reasoningBranchDetailsQuery.findByTreeIdAndBranchId(TREE_ID, BRANCH_NO_1))
          .willReturn(of(new BranchDetailsDto(BRANCH_NO_1, AI_SOLUTION, IS_ACTIVE)));

      get(mappingForBranch(TREE_ID, BRANCH_NO_1))
          .statusCode(OK.value())
          .body("reasoningBranchId", equalTo((int) BRANCH_NO_1))
          .body("aiSolution", equalTo(AI_SOLUTION))
          .body("active", equalTo(IS_ACTIVE));
    }

    @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(mappingForBranch(TREE_ID, BRANCH_NO_1)).statusCode(FORBIDDEN.value());
    }
  }

  @Nested
  class BranchList {

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200WithCorrectBody_whenFound() {
      given(reasoningBranchesQuery.findByTreeIdAndBranchIds(
          TREE_ID, asList(BRANCH_NO_1, BRANCH_NO_2)))
          .willReturn(asList(
              new BranchDto(BRANCH_NO_1, AI_SOLUTION, IS_ACTIVE),
              new BranchDto(BRANCH_NO_2, AI_SOLUTION, IS_ACTIVE)));

      post(mappingForBranches(TREE_ID), LIST_BRANCHES)
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0].reasoningBranchId", equalTo((int) BRANCH_NO_1))
          .body("[0].aiSolution", equalTo(AI_SOLUTION))
          .body("[0].active", equalTo(IS_ACTIVE))
          .body("[1].reasoningBranchId", equalTo((int) BRANCH_NO_2))
          .body("[1].aiSolution", equalTo(AI_SOLUTION))
          .body("[1].active", equalTo(IS_ACTIVE));
    }

    @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      post(mappingForBranches(TREE_ID), LIST_BRANCHES).statusCode(FORBIDDEN.value());
    }
  }

  @Nested
  class BranchesUpdating {

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its404_whenBranchNotFound() {
      given(updateReasoningBranchesUseCase.apply(eq(BRANCHES_UPDATE_COMMAND)))
          .willReturn(failure(BRANCH_NOT_FOUND_EXCEPTION));

      patch(mappingForBranches(TREE_ID), BRANCHES_CHANGE_REQUEST)
          .statusCode(NOT_FOUND.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenNotSupportedAiSolution() {
      given(updateReasoningBranchesUseCase.apply(eq(BRANCHES_UPDATE_COMMAND)))
          .willReturn(failure(AI_SOLUTION_NOT_SUPPORTED_EXCEPTION));

      patch(mappingForBranches(TREE_ID), BRANCHES_CHANGE_REQUEST)
          .statusCode(BAD_REQUEST.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its500_whenUnknownException() {
      given(updateReasoningBranchesUseCase.apply(eq(BRANCHES_UPDATE_COMMAND)))
          .willReturn(failure(UNKNOWN_EXCEPTION));

      patch(mappingForBranches(TREE_ID), BRANCHES_CHANGE_REQUEST)
          .statusCode(INTERNAL_SERVER_ERROR.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200_whenSuccess() {
      given(updateReasoningBranchesUseCase.apply(eq(BRANCHES_UPDATE_COMMAND)))
          .willReturn(Try.success(null));

      patch(mappingForBranches(TREE_ID), BRANCHES_CHANGE_REQUEST)
          .statusCode(OK.value());
    }

    @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      patch(mappingForBranches(TREE_ID), BRANCHES_CHANGE_REQUEST).statusCode(
          FORBIDDEN.value());
    }
  }

  static class ReasoningBranchRestControllerFixtures {

    static final long BRANCH_NO_1 = 5;
    static final long BRANCH_NO_2 = 8;
    static final long TREE_ID = 2;

    static final String AI_SOLUTION = "True Positive";
    static final boolean IS_ACTIVE = false;

    static final ListBranchesRequestDto LIST_BRANCHES =
        new ListBranchesRequestDto(asList(BRANCH_NO_1, BRANCH_NO_2));

    static final BranchesChangesRequestDto BRANCHES_CHANGE_REQUEST =
        new BranchesChangesRequestDto(asList(BRANCH_NO_1, BRANCH_NO_2), AI_SOLUTION, IS_ACTIVE);

    static final UpdateBranchesCommand BRANCHES_UPDATE_COMMAND =
        new UpdateBranchesCommand(
            asList(BranchId.of(TREE_ID, BRANCH_NO_1), BranchId.of(TREE_ID, BRANCH_NO_2)),
            AI_SOLUTION,
            IS_ACTIVE);

    static final BranchNotFoundException BRANCH_NOT_FOUND_EXCEPTION =
        new BranchNotFoundException(new RuntimeException("someCause"));

    static final AiSolutionNotSupportedException AI_SOLUTION_NOT_SUPPORTED_EXCEPTION =
        new AiSolutionNotSupportedException(new RuntimeException("someCause"));

    static final RuntimeException UNKNOWN_EXCEPTION = new RuntimeException();
  }
}
