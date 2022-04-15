package com.silenteight.sens.webapp.scb.user.sync.analyst;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

final class OracleRelationNameValidator implements
    ConstraintValidator<OracleRelationName, String> {

  public static final String SCHEMA_AND_RELATION_NAME_PATTERN =
      "^[a-zA-Z][\\w#$]*(?:\\.[a-zA-Z][\\w#$]*)?$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && Pattern.matches(SCHEMA_AND_RELATION_NAME_PATTERN, value);
  }
}
