package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.FeatureVectorSignaturesNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateBranchesCommand;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdateReasoningBranchesUseCase;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.ReasoningBranchValidator;
import com.silenteight.sens.webapp.backend.rest.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.rest.exception.UnknownExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import io.vavr.control.Try;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchRestControllerTest.ReasoningBranchRestControllerFixtures.*;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static io.vavr.control.Try.failure;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;

@Import({
    ReasoningBranchRestController.class,
    ReasoningBranchRestControllerAdvice.class,
    UnknownExceptionControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class ReasoningBranchRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @MockBean
  private ReasoningBranchesQuery reasoningBranchesQuery;

  @MockBean
  private UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase;

  @MockBean
  private ReasoningBranchValidator reasoningBranchValidator;

  private static String mappingForBranch(long treeId, long branchNo) {
    return format("/decision-trees/%s/branches/%s", treeId, branchNo);
  }

  private static String mappingForBranches(long treeId) {
    return format("/decision-trees/%s/branches", treeId);
  }

  private static String mappingForValidation(long treeId) {
    return format("/decision-trees/%s/branches/validate", treeId);
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
      given(reasoningBranchesQuery.findBranchByTreeIdAndBranchIds(
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
    void its400_whenBranchNotFound() {
      given(updateReasoningBranchesUseCase.apply(eq(BRANCHES_UPDATE_COMMAND)))
          .willReturn(failure(new BranchIdsNotFoundException(List.of(123L, 456L))));

      patch(mappingForBranches(TREE_ID), BRANCHES_CHANGE_REQUEST)
          .body("extras.branchIds", hasItems(123, 456))
          .statusCode(BAD_REQUEST.value());
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

  @Nested
  class BranchesValidation {

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200_whenValidatesBranchIdsOK() {
      List<Long> branchIds = List.of(345L, 678L);
      given(reasoningBranchValidator.validate(TREE_ID, branchIds, null))
          .willReturn(Map.of(345L, "SignatureA", 678L, "SignatureB"));

      put(
          mappingForValidation(TREE_ID), new BranchIdsAndSignaturesDto(branchIds, null))
          .statusCode(OK.value())
          .body("branchIds.branchId", hasItems(345, 678))
          .body("branchIds.featureVectorSignature", hasItems("SignatureA", "SignatureB"));
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its200_whenValidatesFeatureVectorSignaturesOK() {
      List<String> featureVectorSignatures = List.of("SignatureA", "SignatureB");
      given(reasoningBranchValidator.validate(TREE_ID, null, featureVectorSignatures))
          .willReturn(Map.of(346L, "SignatureA", 679L, "SignatureB"));

      put(
          mappingForValidation(TREE_ID),
          new BranchIdsAndSignaturesDto(null, featureVectorSignatures))
          .statusCode(OK.value())
          .body("branchIds.branchId", hasItems(346, 679))
          .body("branchIds.featureVectorSignature", hasItems("SignatureA", "SignatureB"));
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenNeitherBranchIdsNorSignaturesProvided() {
      put(
          mappingForValidation(TREE_ID), new BranchIdsAndSignaturesDto(null, null))
          .statusCode(BAD_REQUEST.value())
          .body(containsString("branchIdsOrFeatureVectorSignatures must be provided"));
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenBranchIdsNotFound() {
      given(reasoningBranchValidator.validate(TREE_ID, List.of(BRANCH_NO_1, BRANCH_NO_2), null))
          .willThrow(new BranchIdsNotFoundException(List.of(123L, 456L)));

      put(
          mappingForValidation(TREE_ID),
          new BranchIdsAndSignaturesDto(List.of(BRANCH_NO_1, BRANCH_NO_2), null))
          .statusCode(BAD_REQUEST.value())
          .body("extras.branchIds", hasItems(123, 456));
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its400_whenFeatureVectorSignaturesNotFound() {
      List<String> signaturesToValidate = List.of("Signature12", "Signature34", "Signature56");
      given(reasoningBranchValidator.validate(TREE_ID, null, signaturesToValidate))
          .willThrow(
              new FeatureVectorSignaturesNotFoundException(List.of("Signature12", "Signature34")));

      put(
          mappingForValidation(TREE_ID),
          new BranchIdsAndSignaturesDto(null, signaturesToValidate))
          .statusCode(BAD_REQUEST.value())
          .body("extras.featureVectorSignatures", hasItems("Signature12", "Signature34"));
    }
  }

  static class ReasoningBranchRestControllerFixtures {

    static final long BRANCH_NO_1 = 5;
    static final long BRANCH_NO_2 = 8;
    static final long TREE_ID = 2;

    static final String AI_SOLUTION = "True Positive";
    static final boolean IS_ACTIVE = false;
    private static final String COMMENT = "comment ABC";

    static final ListBranchesRequestDto LIST_BRANCHES =
        new ListBranchesRequestDto(asList(BRANCH_NO_1, BRANCH_NO_2));

    static final BranchesChangesRequestDto BRANCHES_CHANGE_REQUEST =
        new BranchesChangesRequestDto(
            asList(BRANCH_NO_1, BRANCH_NO_2), AI_SOLUTION, IS_ACTIVE, COMMENT);

    static final UpdateBranchesCommand BRANCHES_UPDATE_COMMAND =
        new UpdateBranchesCommand(TREE_ID, asList(BRANCH_NO_1, BRANCH_NO_2),
            AI_SOLUTION, IS_ACTIVE, COMMENT);

    static final AiSolutionNotSupportedException AI_SOLUTION_NOT_SUPPORTED_EXCEPTION =
        new AiSolutionNotSupportedException(new RuntimeException("someCause"));

    static final RuntimeException UNKNOWN_EXCEPTION = new RuntimeException();
  }
}
