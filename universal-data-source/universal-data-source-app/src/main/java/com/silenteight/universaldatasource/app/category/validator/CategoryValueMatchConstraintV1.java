package com.silenteight.universaldatasource.app.category.validator;

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
@Constraint(validatedBy = CategoryValueMatchValidatorV1.class)
@Documented
public @interface CategoryValueMatchConstraintV1 {

  String message() default
      "Category value match is invalid. Valid format is:"
          + " categories/<Category>/alerts/<Alert ID>/matches/<Match ID>";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
