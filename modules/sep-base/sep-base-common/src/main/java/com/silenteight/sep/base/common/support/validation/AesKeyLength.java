package com.silenteight.sep.base.common.support.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { AesKeyLengthValidator.class })
public @interface AesKeyLength {

  String message() default "Value is not valid AES key length. It should be 128, 192 or 256.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
