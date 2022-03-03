package com.silenteight.customerbridge.common.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class OracleRelationNameValidator
    implements ConstraintValidator<OracleRelationName, String> {

  private static final String SCHEMA_AND_RELATION_NAME_PATTERN =
      "^[a-zA-Z][\\w#$]*(?:\\.[a-zA-Z][\\w#$]*)?$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (isBlank(value))
      return true;

    return Pattern.matches(SCHEMA_AND_RELATION_NAME_PATTERN, value);
  }
}
