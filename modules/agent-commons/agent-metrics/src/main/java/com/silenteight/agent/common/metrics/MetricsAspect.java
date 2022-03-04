package com.silenteight.agent.common.metrics;

import lombok.Setter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Aspect
public class MetricsAspect {

  @Autowired
  @Setter
  private MetricsRecorder recorder;

  @Setter
  @Value("${agents.commons.metrics.enabled}")
  private boolean metricsEnabled;

  @Around("@annotation(com.silenteight.agent.common.metrics.RecordableMetrics)"
      + " && execution(* *(..))")
  public Object recordCounterMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
    if (!metricsEnabled) {
      return joinPoint.proceed();
    }
    for (Object arg : joinPoint.getArgs()) {
      if (!RecordMetrics.class.isAssignableFrom(arg.getClass())) {
        throw new IllegalStateException("Unsupported metrics class: " + arg.getClass());
      }
      var metrics = (RecordMetrics) arg;
      metrics.record(recorder);
    }
    return joinPoint.proceed();
  }
}
