package com.silenteight.payments.bridge.metrics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Measures the time to execute the annotated method and logs it using SLF4J logger.
 * Logger name has a following format: {@link #prefix()}{@link #value()} or
 * only {@link #prefix()} if {@link #value()} is blank.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogMethodTime {

  /**
   * Can be empty.
   * @return Last component of logger name.
   */
  String value() default "";

  /**
   * Prefix of logger name (appended by {@link #value()}.
   * If it is empty then class name is used.
   */
  String prefix() default "";
}
