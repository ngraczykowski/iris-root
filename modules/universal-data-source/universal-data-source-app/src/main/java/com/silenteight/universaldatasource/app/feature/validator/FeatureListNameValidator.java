package com.silenteight.universaldatasource.app.feature.validator;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class FeatureListNameValidator
    implements ConstraintValidator<FeatureListNameConstraint, List<String>> {

  private static final String FEATURE_PREFIX = "features/";

  @Override
  public boolean isValid(
      List<String> featureNames, ConstraintValidatorContext constraintValidatorContext) {
    return featureNames.stream().allMatch(f -> f.startsWith(FEATURE_PREFIX));
  }
}
