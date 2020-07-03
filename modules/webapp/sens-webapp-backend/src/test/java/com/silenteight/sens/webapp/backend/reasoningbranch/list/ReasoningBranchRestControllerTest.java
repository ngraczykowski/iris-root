package com.silenteight.sens.webapp.backend.reasoningbranch.list;

import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.*;
import com.silenteight.sens.webapp.backend.support.Paging;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.grpc.InvalidBranchSolutionException;

import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.reasoningbranch.list.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.*;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.String.format;
import static java.time.Instant.parse;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ReasoningBranchRestController.class, ReasoningBranchListRestControllerAdvice.class })
class ReasoningBranchRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReasoningBranchesQuery reasoningBranchesQuery;

  @Nested
  class BranchList {

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200WithCorrectBodyWithFilter_whenFound() {
      given(
          reasoningBranchesQuery.list(
              new ReasoningBranchFilterDto(AI_SOLUTION, IS_ACTIVE), new Paging(0, PAGE_SIZE)))
          .willReturn(
              new ReasoningBranchesPageDto(asList(REASONING_BRANCH_1, REASONING_BRANCH_2), 2));

      validateResponse(get(mappingForReasoningBranches(LIST_BRANCHES_REQUEST)));
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200WithCorrectBodyWithoutFilter_whenFound() {
      given(
          reasoningBranchesQuery.list(
              new ReasoningBranchFilterDto(null, null), new Paging(0, PAGE_SIZE)))
          .willReturn(
              new ReasoningBranchesPageDto(asList(REASONING_BRANCH_1, REASONING_BRANCH_2), 2));

      validateResponse(get(mappingForReasoningBranches(0, PAGE_SIZE)));
    }

    private void validateResponse(ValidatableMockMvcResponse response) {
      response
          .statusCode(OK.value())
          .body("total", is(2))
          .body("branches[0].reasoningBranchId.decisionTreeId", equalTo((int) TREE_ID))
          .body("branches[0].reasoningBranchId.featureVectorId",
              equalTo((int) FEATURE_VECTOR_ID_1))
          .body("branches[0].aiSolution", equalTo(AI_SOLUTION))
          .body("branches[0].active", equalTo(IS_ACTIVE))
          .body("branches[0].updatedAt", notNullValue())
          .body("branches[1].reasoningBranchId.decisionTreeId", equalTo((int) TREE_ID))
          .body("branches[1].reasoningBranchId.featureVectorId",
              equalTo((int) FEATURE_VECTOR_ID_2))
          .body("branches[1].aiSolution", equalTo(AI_SOLUTION))
          .body("branches[1].active", equalTo(IS_ACTIVE))
          .body("branches[1].createdAt", notNullValue())
          .body("branches[1].updatedAt", notNullValue());
    }

    @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR, APPROVER })
    void its403_whenNotPermittedRole() {
      get(mappingForReasoningBranches(LIST_BRANCHES_REQUEST))
          .statusCode(FORBIDDEN.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenInvalidSolution() {
      given(
          reasoningBranchesQuery.list(
              new ReasoningBranchFilterDto(INVALID_AI_SOLUTION, IS_ACTIVE),
              new Paging(0, PAGE_SIZE)))
          .willThrow(InvalidBranchSolutionException.class);

      get(mappingForReasoningBranches(INVALID_LIST_BRANCHES_REQUEST))
          .statusCode(BAD_REQUEST.value())
          .body("key", equalTo("InvalidBranchSolution"))
          .body("extras.solution", equalTo("null"));
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenInvalidLimit() {
      get(mappingForReasoningBranches(0, 0))
          .statusCode(BAD_REQUEST.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenInvalidOffset() {
      get(mappingForReasoningBranches(-10, 10))
          .statusCode(BAD_REQUEST.value());
    }

    private String mappingForReasoningBranches(ListReasoningBranchesRequestDto request) {
      return format(
          "/reasoning-branches?aiSolution=%s&active=%s&offset=%d&limit=%d",
          request.getAiSolution(),
          request.getActive(),
          request.getOffset(),
          request.getLimit());
    }

    private String mappingForReasoningBranches(int offset, int limit) {
      return format("/reasoning-branches?offset=%d&limit=%d", offset, limit);
    }
  }

  static class ReasoningBranchRestControllerFixtures {

    static final long TREE_ID = 2;
    static final long FEATURE_VECTOR_ID_1 = 5;
    static final long FEATURE_VECTOR_ID_2 = 8;
    static final String AI_SOLUTION = "TRUE_POSITIVE";
    static final String INVALID_AI_SOLUTION = "UNKNOWN";
    static final boolean IS_ACTIVE = true;

    static final int PAGE_SIZE = 20;

    static final ListReasoningBranchesRequestDto LIST_BRANCHES_REQUEST =
        new ListReasoningBranchesRequestDto(AI_SOLUTION, IS_ACTIVE, 0, PAGE_SIZE);

    static final ListReasoningBranchesRequestDto INVALID_LIST_BRANCHES_REQUEST =
        new ListReasoningBranchesRequestDto(INVALID_AI_SOLUTION, IS_ACTIVE, 0, PAGE_SIZE);

    static final ReasoningBranchDto REASONING_BRANCH_1 =
        ReasoningBranchDto
            .builder()
            .reasoningBranchId(new ReasoningBranchIdDto(TREE_ID, FEATURE_VECTOR_ID_1))
            .aiSolution(AI_SOLUTION)
            .active(IS_ACTIVE)
            .createdAt(parse("2020-04-12T11:25:30Z"))
            .updatedAt(parse("2020-05-03T10:15:30Z"))
            .build();

    static final ReasoningBranchDto REASONING_BRANCH_2 =
        ReasoningBranchDto
            .builder()
            .reasoningBranchId(new ReasoningBranchIdDto(TREE_ID, FEATURE_VECTOR_ID_2))
            .aiSolution(AI_SOLUTION)
            .active(IS_ACTIVE)
            .createdAt(parse("2020-04-12T11:25:30Z"))
            .updatedAt(parse("2020-06-12T09:25:45Z"))
            .build();
  }
}
