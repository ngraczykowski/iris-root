package com.silenteight.customerbridge.common.recommendation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = { RecommendationRequestValidator.class })
public @interface ValidRecommendationRequest {

  String message() default "Request for recommendation is invalid. "
      + "Either systemId or unit, account, recordDetails must be present.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
