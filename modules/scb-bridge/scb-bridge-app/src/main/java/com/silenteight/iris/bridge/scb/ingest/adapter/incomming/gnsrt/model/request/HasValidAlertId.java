/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PotentialMatchAlertIdValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
@interface HasValidAlertId {

  String message() default "Alert has invalid id.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
