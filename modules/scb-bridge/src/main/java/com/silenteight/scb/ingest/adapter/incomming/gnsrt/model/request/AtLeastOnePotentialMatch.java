package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator annotation checking whether gns alert list collection has at least one POTENTIAL
 * MATCH.
 */
@Documented
@Constraint(validatedBy = AtLeastOnePotentialMatchValidator.class)
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
@interface AtLeastOnePotentialMatch {

  String message() default "No Alerts with status POTENTIAL_MATCH.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
