package com.silenteight.searpayments.bridge.trash;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.searpayments.bridge.dto.input.RequestDto;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;

@Aspect
@RequiredArgsConstructor
@Slf4j
class LogRequestAspect {

  @Pointcut("execution(* com.silenteight.searpaymentsmockup2.AlertController.foo(..))"
      + " && args(requestDto, dc)")
  public void callAt(RequestDto requestDto, String dc) {
    throw new UnsupportedOperationException();
  }

  @Around("callAt(requestDto, dc)")
  public Object aroundCallAt(
      ProceedingJoinPoint pjp, RequestDto requestDto, String dc) throws Throwable {

    log.info("Logging request to database here. requestDto: {}, dc: {}",
        requestDto, dc);
    return pjp.proceed();
  }

}
