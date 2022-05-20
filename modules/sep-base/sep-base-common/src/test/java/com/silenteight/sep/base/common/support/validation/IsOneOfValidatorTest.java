package com.silenteight.sep.base.common.support.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;

import static java.util.stream.Stream.of;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IsOneOfValidatorTest {

  @Mock
  private IsOneOf isOneOf;

  private final IsOneOfValidator underTest = new IsOneOfValidator();

  @Nested
  class GivenOnlyAesKeyLengths extends BaseValidatorTest<IsOneOf, Integer> {

    @BeforeEach
    void setUp() {
      given(isOneOf.value()).willReturn(new int[] { 128, 192, 256 });
      underTest.initialize(isOneOf);
    }

    @Override
    protected Supplier<ConstraintValidator<IsOneOf, Integer>> getUnderTest() {
      return () -> underTest;
    }

    @Override
    protected Stream<Integer> getValidValues() {
      return of(128, 192, 256);
    }

    @Override
    protected Stream<Integer> getInvalidValues() {
      return of(122, 129, 255, 289, 353, 999999, 0, 1, -123);
    }
  }
}
