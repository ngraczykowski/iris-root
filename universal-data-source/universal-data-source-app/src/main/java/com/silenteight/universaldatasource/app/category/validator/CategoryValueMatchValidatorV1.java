package com.silenteight.universaldatasource.app.category.validator;

import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryValueMatchValidatorV1
    implements ConstraintValidator<CategoryValueMatchConstraintV1, List<String>> {

  private static final String CATEGORY_VALUE_MATCH = "categories/\\w+/alerts/\\d+/matches/\\d+";
  private static final Pattern CATEGORY_VALUE_MATCH_PATTERN = Pattern.compile(CATEGORY_VALUE_MATCH);

  @Override
  public boolean isValid(
      List<String> categoryValueMatches, ConstraintValidatorContext constraintValidatorContext) {
    return categoryValueMatches.stream()
        .allMatch(c -> CATEGORY_VALUE_MATCH_PATTERN.matcher(c).find());
  }
}
