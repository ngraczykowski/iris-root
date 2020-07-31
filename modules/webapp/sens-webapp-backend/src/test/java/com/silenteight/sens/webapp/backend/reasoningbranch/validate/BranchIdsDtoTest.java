package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdsAndSignaturesDto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.*;

class BranchIdsDtoTest {

  private static final Validator VALIDATOR = buildDefaultValidatorFactory().getValidator();

  @Test
  void validatesOkIfBranchIdsProvided() {
    BranchIdsAndSignaturesDto branchIdsDto = new BranchIdsAndSignaturesDto(List.of(1L), null);
    Set<ConstraintViolation<BranchIdsAndSignaturesDto>> violations =
        VALIDATOR.validate(branchIdsDto);

    assertThat(violations).isEmpty();
  }

  @Test
  void validatesOkIfSignaturesProvided() {
    BranchIdsAndSignaturesDto branchIdsDto =
        new BranchIdsAndSignaturesDto(null, List.of("signature1"));
    Set<ConstraintViolation<BranchIdsAndSignaturesDto>> violations =
        VALIDATOR.validate(branchIdsDto);

    assertThat(violations).isEmpty();
  }

  @Test
  void constraintViolationIfNoBranchIdsNorSignaturesProvided() {
    BranchIdsAndSignaturesDto branchIdsDto = new BranchIdsAndSignaturesDto(null, null);
    Set<ConstraintViolation<BranchIdsAndSignaturesDto>> violations =
        VALIDATOR.validate(branchIdsDto);

    assertThat(violations).hasSize(1);
    ConstraintViolation<BranchIdsAndSignaturesDto> violation = violations.iterator().next();
    assertThat(violation.getPropertyPath().toString())
        .isEqualTo("branchIdsOrFeatureVectorSignatures");
    assertThat(violation.getMessage()).isEqualTo("must be provided");
  }

  @Test
  void constraintViolationIfBothBranchIdsAndSignaturesProvided() {
    BranchIdsAndSignaturesDto branchIdsDto =
        new BranchIdsAndSignaturesDto(List.of(3L), List.of("signature2"));
    Set<ConstraintViolation<BranchIdsAndSignaturesDto>> violations =
        VALIDATOR.validate(branchIdsDto);

    assertThat(violations).hasSize(1);
    ConstraintViolation<BranchIdsAndSignaturesDto> violation = violations.iterator().next();
    assertThat(violation.getPropertyPath().toString())
        .isEqualTo("branchIdsOrFeatureVectorSignatures");
    assertThat(violation.getMessage()).isEqualTo("must be provided");
  }
}
