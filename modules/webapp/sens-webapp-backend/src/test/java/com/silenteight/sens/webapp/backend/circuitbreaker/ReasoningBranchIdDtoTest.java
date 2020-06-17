package com.silenteight.sens.webapp.backend.circuitbreaker;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ReasoningBranchIdDtoTest {

  @Test
  void createsObjectFromDashSeparatedBranchId() {
    ReasoningBranchIdDto reasoningBranchIdDto = ReasoningBranchIdDto.valueOf("1-2");

    assertThat(reasoningBranchIdDto.getDecisionTreeId()).isEqualTo(1L);
    assertThat(reasoningBranchIdDto.getFeatureVectorId()).isEqualTo(2L);
  }

  @Test
  void throwsExceptionIfBranchIdNotDashSeparated() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdDto.valueOf("12");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidBranchIdException.class);
  }

  @Test
  void throwsExceptionIfTooManyDashes() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdDto.valueOf("1-2-3");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidBranchIdException.class);
  }

  @Test
  void throwsExceptionIfDecisionTreeNotNumeric() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdDto.valueOf("1a-2");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidBranchIdException.class);
  }

  @Test
  void throwsExceptionIfFeatureVectorNotNumeric() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdDto.valueOf("1-2a");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidBranchIdException.class);
  }
}
