package com.silenteight.sens.webapp.scb.user.sync.analyst;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = OracleRelationNameValidator.class)
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
@interface OracleRelationName {

  String message() default "Invalid Oracle database relation name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
