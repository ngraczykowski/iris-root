package com.silenteight.sens.webapp.aspects.logging;

import com.silenteight.sens.webapp.aspects.logging.LogContext.LogContextAction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * AspectJ aspect for managing the contents of Slf4j's {@link MDC}.
 * <p/>
 * To use it, add {@link LogContext} annotation to the method, that you want MDC cleared.
 */
@Aspect
public class LogContextAspect {

  @Around("execution (@com.silenteight.sens.webapp.aspects.logging.LogContext * *.*(..))")
  public Object logContextMethod(ProceedingJoinPoint pjp) throws Throwable {
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    LogContext logContext = method.getAnnotation(LogContext.class);
    if (logContext == null) {
      method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
      logContext = method.getAnnotation(LogContext.class);
    }

    if (logContext.value() == LogContextAction.CLEAR) {
      MDC.clear();
      return pjp.proceed();
    }

    Map<String, String> savedContext = MDC.getCopyOfContextMap();

    try {
      if (logContext.value().isClear())
        MDC.clear();

      return pjp.proceed();
    } finally {
      if (logContext.value().isMerge()) {
        Map<String, String> mergedContext = MDC.getCopyOfContextMap();
        if (mergedContext != null) {
          if (savedContext != null)
            savedContext.putAll(mergedContext);
          else
            savedContext = mergedContext;
        }
      }

      if (savedContext != null)
        MDC.setContextMap(savedContext);
      else
        MDC.clear();
    }
  }
}
