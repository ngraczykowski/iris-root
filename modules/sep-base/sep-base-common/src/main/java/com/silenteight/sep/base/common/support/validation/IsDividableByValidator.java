package com.silenteight.sep.base.common.support.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsDividableByValidator implements ConstraintValidator<IsDividableBy, Integer> {

  private int dividableBy;

  @Override
  public void initialize(IsDividableBy isDividableByAnnotation) {
    dividableBy = isDividableByAnnotation.value();
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext ctx) {
    return value % dividableBy == 0;
  }
}
