package com.silenteight.customerbridge.common.recommendation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class RecommendationRequestValidator
    implements ConstraintValidator<ValidRecommendationRequest, RecommendationRequest> {

  @Override
  public boolean isValid(
      RecommendationRequest request, ConstraintValidatorContext constraintValidatorContext) {
    return request.hasSystemId() || request.hasAllDetails();
  }
}
