package com.silenteight.sens.webapp.aspects.metrics;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD })
@Repeatable(TimedSet.class)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Timed {

  /**
   * Name of the Timer metric.
   *
   * @return name of the Timer metric
   */
  String value() default "";

  String[] extraTags() default {};

  double[] percentiles() default {};

  boolean histogram() default false;

  String description() default "";
}
