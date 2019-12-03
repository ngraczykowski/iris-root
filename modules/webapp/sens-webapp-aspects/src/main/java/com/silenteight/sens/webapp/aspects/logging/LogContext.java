package com.silenteight.sens.webapp.aspects.logging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogContext {

  LogContextAction value() default LogContextAction.PRESERVE;

  /**
   * MDC clearing options.
   */
  @RequiredArgsConstructor
  @Getter(AccessLevel.PACKAGE)
  enum LogContextAction {

    /**
     * Logging context cleared before entering the method.
     */
    CLEAR(true, false),

    /**
     * Logging context cleared for method, original logging context restored after exiting
     * the method.
     */
    CLEAR_PRESERVE(true, false),

    /**
     * Original logging context restored after exiting the method.
     */
    PRESERVE(false, false),

    /**
     * After returning, the contents of the context gets merged with preserved context.
     */
    PRESERVE_MERGE(false, true),

    /**
     * Method gets clear logging context. After returning, the contents of the context gets merged
     * with preserved context.
     */
    CLEAR_PRESERVE_MERGE(true, true);

    private final boolean clear;
    private final boolean merge;
  }
}
