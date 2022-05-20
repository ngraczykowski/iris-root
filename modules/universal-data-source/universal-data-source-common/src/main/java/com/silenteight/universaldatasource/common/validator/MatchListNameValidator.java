package com.silenteight.universaldatasource.common.validator;

import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class MatchListNameValidator
    implements ConstraintValidator<MatchListNameConstraint, List<String>> {

  private static final String MATCH_REGEX = "alerts/\\d+/matches/\\d+";
  private static final Pattern MATCH_PATTERN = Pattern.compile(MATCH_REGEX);

  @Override
  public boolean isValid(
      List<String> matchNames, ConstraintValidatorContext constraintValidatorContext) {
    return matchNames
        .stream()
        .allMatch(MatchListNameValidator::isMatchValid);
  }

  private static boolean isMatchValid(String matchName) {
    return MATCH_PATTERN.matcher(matchName).find();
  }
}
