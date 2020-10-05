package com.silenteight.sep.base.common.support.validation;

import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;

import static java.util.stream.Stream.of;


class AesKeyLengthValidatorTest extends BaseValidatorTest<AesKeyLength, Integer> {

  private final AesKeyLengthValidator underTest = new AesKeyLengthValidator();

  @Override
  protected Supplier<ConstraintValidator<AesKeyLength, Integer>> getUnderTest() {
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
