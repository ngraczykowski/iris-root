package com.silenteight.sep.base.aspects.metrics;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TimedSet {

  Timed[] value();
}
