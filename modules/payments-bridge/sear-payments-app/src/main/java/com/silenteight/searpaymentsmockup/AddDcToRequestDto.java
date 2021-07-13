package com.silenteight.searpaymentsmockup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
@RequiredArgsConstructor
class AddDcToRequestDto {

  @Pointcut("execution(* com.silenteight.searpaymentsmockup2.AlertController.foo(..))"
      + " && args(requestDto, dc)")
  public void callAt(RequestDto requestDto, String dc) {
    throw new UnsupportedOperationException();
  }

  @Around("callAt(requestDto, dc)")
  public Object aroundCallAt(ProceedingJoinPoint pjp, RequestDto requestDto,
      String dc) throws Throwable {

    requestDto.setDc(dc);
    log.info("Added DC {} to requestDto", dc);
    return pjp.proceed();
  }

}
