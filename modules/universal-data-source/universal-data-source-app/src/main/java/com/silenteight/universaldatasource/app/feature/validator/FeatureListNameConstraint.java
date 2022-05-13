package com.silenteight.universaldatasource.app.feature.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FeatureListNameValidator.class)
@Documented
public @interface FeatureListNameConstraint {

  String message() default "Feature name is invalid. Valid format is: features/<Feature ID>";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
