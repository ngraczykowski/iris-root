package com.silenteight.searpayments.bridge.metrics;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Aspect
public class LogMethodTimeAspect {


  private static final String METHOD_NAME_KEY = "methodName";
  private static final String COMPLETED_IN_MILLIS_KEY = "completedInMillis";

  @Pointcut("@annotation(logMethodTime) && execution(* *.*(..))")
  public void timer(LogMethodTime logMethodTime) {
  }

  @Around("timer(logMethodTime)")
  public Object aroundTimer(ProceedingJoinPoint pjp, LogMethodTime logMethodTime) throws
      Throwable {

    var className = pjp.getStaticPart().getSignature().getDeclaringType().getName();
    var loggerName = buildLoggerName(
        className, logMethodTime.prefix(), logMethodTime.value());

    Logger logger = LoggerFactory.getLogger(loggerName);
    long beginTs = System.currentTimeMillis();
    try {
      return pjp.proceed();
    } finally {
      maybeDebugLog(logger, pjp, beginTs);
    }
  }

  private static void maybeDebugLog(Logger logger, ProceedingJoinPoint pjp, long beginTs) {
    if (logger.isDebugEnabled()) {
      long completedInMillis = System.currentTimeMillis() - beginTs;
      var method = ((MethodSignature) pjp.getSignature()).getMethod();
      var methodName = method.getName();

      try {
        MDC.put(METHOD_NAME_KEY, methodName);
        MDC.put(COMPLETED_IN_MILLIS_KEY, String.valueOf(completedInMillis));
        logger.debug("Method {} completed in {}ms", methodName, completedInMillis);
      } finally {
        MDC.remove(METHOD_NAME_KEY);
        MDC.remove(COMPLETED_IN_MILLIS_KEY);
      }
    }
  }

  static String buildLoggerName(
      @NonNull String className, @Nullable String prefix, @Nullable String value) {

    boolean prefixIsBlank = StringUtils.isBlank(prefix);
    boolean valueIsBlank = StringUtils.isBlank(value);

    if (!prefixIsBlank && !valueIsBlank) {
      return String.join(".", prefix.trim(), value.trim());
    } else if (prefixIsBlank && !valueIsBlank) {
      return String.join(".", className.trim(), value.trim());
    } else if (!prefixIsBlank) {
      return prefix.trim();
    } else {
      return className.trim();
    }
  }

}
