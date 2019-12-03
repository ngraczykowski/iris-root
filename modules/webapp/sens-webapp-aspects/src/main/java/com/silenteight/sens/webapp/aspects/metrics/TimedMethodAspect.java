package com.silenteight.sens.webapp.aspects.metrics;

import lombok.Setter;

import io.micrometer.core.instrument.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * AspectJ aspect for intercepting types or methods annotated with {@link Timed @Timed}.
 *
 * @author David J. M. Karlsen
 * @author Jon Schneider
 * @author Johnny Lim
 * @author Andrzej Haczewski
 */
@Aspect
public class TimedMethodAspect {

  private static final String DEFAULT_METRIC_NAME = "method.timed";

  /**
   * Tag key for an exception.
   */
  private static final String EXCEPTION_TAG = "exception";

  @Setter
  private Function<ProceedingJoinPoint, Iterable<Tag>> tagsBasedOnJoinPoint;
  @Setter
  private MeterRegistry registry = Metrics.globalRegistry;

  public TimedMethodAspect() {
    tagsBasedOnJoinPoint = pjp ->
        Tags.of("class", pjp.getStaticPart().getSignature().getDeclaringTypeName(),
                "method", pjp.getStaticPart().getSignature().getName());
  }

  @Around("execution (@com.silenteight.sens.webapp.aspects.metrics.Timed * *.*(..))")
  public Object timedMethod(ProceedingJoinPoint pjp) throws Throwable {
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    Timed timed = method.getAnnotation(Timed.class);
    if (timed == null) {
      method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
      timed = method.getAnnotation(Timed.class);
    }

    Timer.Sample sample = Timer.start(registry);
    String exceptionClassName = "none";

    try {
      return pjp.proceed();
    } catch (Exception ex) {
      exceptionClassName = ex.getClass().getSimpleName();
      throw ex;
    } finally {
      try {
        String metricName = timed.value().isEmpty() ? DEFAULT_METRIC_NAME : timed.value();

        Timer timer = Timer
            .builder(metricName)
            .description(timed.description().isEmpty() ? null : timed.description())
            .tags(timed.extraTags())
            .tags(EXCEPTION_TAG, exceptionClassName)
            .tags(tagsBasedOnJoinPoint.apply(pjp))
            .publishPercentileHistogram(timed.histogram())
            .publishPercentiles(timed.percentiles().length == 0 ? null : timed.percentiles())
            .register(registry);

        sample.stop(timer);
      } catch (Exception ignored) {
        // ignoring on purpose
      }
    }
  }
}
