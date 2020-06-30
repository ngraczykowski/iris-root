package com.silenteight.sens.webapp.backend.parser;

import com.silenteight.sens.webapp.backend.parser.exception.InvalidReasoningBranchIdException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ReasoningBranchIdParserTest {

  @Test
  void createsObjectFromDashSeparatedBranchId() {
    ParsedReasoningBranchId reasoningBranchId = ReasoningBranchIdParser.parse("1-2");

    assertThat(reasoningBranchId.getDecisionTreeId()).isEqualTo(1L);
    assertThat(reasoningBranchId.getFeatureVectorId()).isEqualTo(2L);
  }

  @Test
  void throwsExceptionIfBranchIdNotDashSeparated() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdParser.parse("12");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidReasoningBranchIdException.class);
  }

  @Test
  void throwsExceptionIfTooManyDashes() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdParser.parse("1-2-3");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidReasoningBranchIdException.class);
  }

  @Test
  void throwsExceptionIfDecisionTreeNotNumeric() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdParser.parse("1a-2");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidReasoningBranchIdException.class);
  }

  @Test
  void throwsExceptionIfFeatureVectorNotNumeric() {
    ThrowingCallable valueOfCall = () -> ReasoningBranchIdParser.parse("1-2a");

    assertThatThrownBy(valueOfCall).isInstanceOf(InvalidReasoningBranchIdException.class);
  }
}
