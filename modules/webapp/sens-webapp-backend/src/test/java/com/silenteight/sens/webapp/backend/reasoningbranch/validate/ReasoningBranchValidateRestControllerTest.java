package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.config.exception.UnknownExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdsAndSignaturesDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.exception.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.exception.FeatureVectorSignaturesNotFoundException;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ReasoningBranchValidateRestController.class,
    ReasoningBranchValidationRestControllerAdvice.class,
    UnknownExceptionControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class ReasoningBranchValidateRestControllerTest extends BaseRestControllerTest {

  static final long BRANCH_NO_1 = 5;
  static final long BRANCH_NO_2 = 8;
  static final long TREE_ID = 2;

  @MockBean
  private ReasoningBranchValidator reasoningBranchValidator;

  private static String mappingForValidation(long treeId) {
    return format("/decision-trees/%s/branches/validate", treeId);
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
}
