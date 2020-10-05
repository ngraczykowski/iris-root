package com.silenteight.sep.base.common.support.validation;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AesKeyLengthValidator implements ConstraintValidator<AesKeyLength, Integer> {

  private static final List<Integer> POSSIBLE_AES_KEY_LENGTHS = List.of(128, 192, 256);

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return POSSIBLE_AES_KEY_LENGTHS.contains(value);
  }
}
