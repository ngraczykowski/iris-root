package com.silenteight.sep.base.common.support.validation;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsOneOfValidator implements ConstraintValidator<IsOneOf, Integer> {

  private int[] givenValues;

  @Override
  public void initialize(
      IsOneOf constraintAnnotation) {
    givenValues = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return Arrays.stream(givenValues).anyMatch(givenValue -> givenValue == value);
  }
}
