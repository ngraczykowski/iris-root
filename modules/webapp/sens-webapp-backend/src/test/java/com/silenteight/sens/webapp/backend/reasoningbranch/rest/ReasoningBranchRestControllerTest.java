package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchCommand;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchUseCase;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import io.vavr.control.Try;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.*;
import static io.vavr.control.Try.failure;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({ ReasoningBranchRestController.class })
class ReasoningBranchRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @MockBean
  private UpdateReasoningBranchUseCase updateReasoningBranchUseCase;

  private static String mappingForBranch(long treeId, long branchNo) {
    return format("/decision-trees/%s/branches/%s", treeId, branchNo);
  }

  @Nested
  class BranchDetails {

    @Test
    void its404_whenNonExistingReasoningBranch() {
      given(reasoningBranchDetailsQuery.findByTreeIdAndBranchId(TREE_ID, BRANCH_NO))
          .willReturn(empty());

      get(mappingForBranch(TREE_ID, BRANCH_NO))
          .contentType(anything())
          .statusCode(NOT_FOUND.value());
    }

    @Test
    void its200WithCorrectBody_whenFound() {
      given(reasoningBranchDetailsQuery.findByTreeIdAndBranchId(TREE_ID, BRANCH_NO))
          .willReturn(of(new BranchDetailsDto(BRANCH_NO, AI_SOLUTION, IS_ACTIVE)));

      get(mappingForBranch(TREE_ID, BRANCH_NO))
          .statusCode(OK.value())
          .body("reasoningBranchId", equalTo((int) BRANCH_NO))
          .body("aiSolution", equalTo(AI_SOLUTION))
          .body("active", equalTo(IS_ACTIVE));
    }
  }

  @Nested
  class BranchUpdating {

    @Test
    void its404_whenBranchNotFound() {
      given(updateReasoningBranchUseCase.apply(eq(BRANCH_UPDATE_COMMAND)))
          .willReturn(failure(BRANCH_NOT_FOUND_EXCEPTION));

      patch(mappingForBranch(TREE_ID, BRANCH_NO), BRANCH_CHANGE_REQUEST)
          .statusCode(NOT_FOUND.value());
    }

    @Test
    void its400_whenNotSupportedAiSolution() {
      given(updateReasoningBranchUseCase.apply(eq(BRANCH_UPDATE_COMMAND)))
          .willReturn(failure(AI_SOLUTION_NOT_SUPPORTED_EXCEPTION));

      patch(mappingForBranch(TREE_ID, BRANCH_NO), BRANCH_CHANGE_REQUEST)
          .statusCode(BAD_REQUEST.value());
    }

    @Test
    void its500_whenUnknownException() {
      given(updateReasoningBranchUseCase.apply(eq(BRANCH_UPDATE_COMMAND)))
          .willReturn(failure(UNKNOWN_EXCEPTION));

      patch(mappingForBranch(TREE_ID, BRANCH_NO), BRANCH_CHANGE_REQUEST)
          .statusCode(INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void its200_whenSuccess() {
      given(updateReasoningBranchUseCase.apply(eq(BRANCH_UPDATE_COMMAND)))
          .willReturn(Try.success(null));

      patch(mappingForBranch(TREE_ID, BRANCH_NO), BRANCH_CHANGE_REQUEST)
          .statusCode(OK.value());
    }
  }

  static class ReasoningBranchRestControllerFixtures {

    static final long BRANCH_NO = 5;
    static final long TREE_ID = 2;

    static final String AI_SOLUTION = "True Positive";
    static final boolean IS_ACTIVE = false;

    static final BranchChangesRequestDto BRANCH_CHANGE_REQUEST = new BranchChangesRequestDto(
        AI_SOLUTION, IS_ACTIVE);

    static final UpdateBranchCommand
        BRANCH_UPDATE_COMMAND =
        new UpdateBranchCommand(BranchId.of(TREE_ID, BRANCH_NO), AI_SOLUTION, IS_ACTIVE);

    static final BranchNotFoundException BRANCH_NOT_FOUND_EXCEPTION =
        new BranchNotFoundException(new RuntimeException("someCause"));
    static final AiSolutionNotSupportedException AI_SOLUTION_NOT_SUPPORTED_EXCEPTION =
        new AiSolutionNotSupportedException(new RuntimeException("someCause"));
    static final RuntimeException UNKNOWN_EXCEPTION = new RuntimeException();
  }
}
