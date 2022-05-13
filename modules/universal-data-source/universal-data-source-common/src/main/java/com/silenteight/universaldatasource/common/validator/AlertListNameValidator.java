package com.silenteight.universaldatasource.common.validator;

import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class AlertListNameValidator
    implements ConstraintValidator<AlertListNameConstraint, List<String>> {

  private static final String ALERT_REGEX = "alerts/\\d+";
  private static final Pattern ALERT_PATTERN = Pattern.compile(ALERT_REGEX);

  @Override
  public boolean isValid(
      List<String> alertNames, ConstraintValidatorContext constraintValidatorContext) {
    return alertNames.stream().allMatch(AlertListNameValidator::isAlertValid);
  }

  private static boolean isAlertValid(String alertName) {
    return ALERT_PATTERN.matcher(alertName).find();
  }

}
