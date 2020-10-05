package com.silenteight.sep.base.common.support.validation;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Min(32)
@Max(128)
@IsDividableBy(8)
@Constraint(validatedBy = {})
@Documented
@Target({ FIELD })
@Retention(RUNTIME)
public @interface MacLength {

  String message() default "Value is not a valid MAC size. "
      + "It should be >= 32, <= 128 and dividable by 8.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
