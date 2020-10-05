package com.silenteight.sep.base.common.support.validation;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(MockitoExtension.class)
abstract class BaseValidatorTest<A extends Annotation, T> {

  @Mock
  protected ConstraintValidatorContext context;

  protected abstract Supplier<ConstraintValidator<A, T>> getUnderTest();

  protected abstract Stream<T> getValidValues();

  protected abstract Stream<T> getInvalidValues();

  @TestFactory
  Stream<DynamicTest> givenWrongSize_returnsFalse() {
    return getInvalidValues()
        .map(testValue -> dynamicTest(
            "Given " + testValue + " should fail",
            () -> assertThat(executeUnderTest(testValue)).isFalse()));
  }


  boolean executeUnderTest(T value) {
    return getUnderTest().get().isValid(value, context);
  }

  @TestFactory
  Stream<DynamicTest> givenCorrectSize_returnsTrue() {
    return getValidValues()
        .map(testValue -> dynamicTest(
            "Given " + testValue + " should pass",
            () -> assertThat(executeUnderTest(testValue)).isTrue()));
  }
}
