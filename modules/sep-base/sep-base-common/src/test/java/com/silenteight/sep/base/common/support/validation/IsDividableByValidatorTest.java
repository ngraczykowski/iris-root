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
class IsDividableByValidatorTest {

  @Mock
  private IsDividableBy isDividableBy;

  @Nested
  class GivenIsDividableBy4 extends BaseValidatorTest<IsDividableBy, Integer> {

    private final IsDividableByValidator underTest = new IsDividableByValidator();

    @BeforeEach
    void setUp() {
      given(isDividableBy.value()).willReturn(4);
      underTest.initialize(isDividableBy);
    }

    @Override
    protected Stream<Integer> getInvalidValues() {
      return of(3, 5, -21, 56373, 999999);
    }

    @Override
    protected Supplier<ConstraintValidator<IsDividableBy, Integer>> getUnderTest() {
      return () -> underTest;
    }

    @Override
    protected Stream<Integer> getValidValues() {
      return of(4, -4, -64, 12, 16, 888888, 444444);
    }
  }
}
