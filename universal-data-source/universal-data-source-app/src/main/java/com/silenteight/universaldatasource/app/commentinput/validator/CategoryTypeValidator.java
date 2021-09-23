package com.silenteight.universaldatasource.app.commentinput.validator;

import com.silenteight.datasource.categories.api.v2.CategoryType;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryTypeValidator implements
    ConstraintValidator<CategoryTypeConstraint, CategoryType> {

  private List<String> acceptedValues;

  @Override
  public void initialize(CategoryTypeConstraint constraintAnnotation) {
    acceptedValues = List.of(CategoryType.ENUMERATED.name(), CategoryType.ANY_STRING.name());
  }

  @Override
  public boolean isValid(
      CategoryType value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) {
      return false;
    }

    return acceptedValues.contains(value.toString());
  }


}
