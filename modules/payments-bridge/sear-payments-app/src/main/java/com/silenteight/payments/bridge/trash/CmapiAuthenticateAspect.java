package com.silenteight.payments.bridge.trash;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.dto.input.RequestDto;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Arrays.asList;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class CmapiAuthenticateAspect {

  @Pointcut("execution(* com.silenteight.payments.bridge.AlertController.foo(..))"
      + " && args(requestDto, ..)")
  public void callAt(RequestDto requestDto) {
    throw new UnsupportedOperationException();
  }

  @Around("callAt(requestDto)")
  public Object aroundCallAt(
      ProceedingJoinPoint pjp, RequestDto requestDto) throws Throwable {

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("user", "password",
            asList(new SimpleGrantedAuthority("ROLE_SUBMIT_REQUEST"))));
    log.info("I authenticated user: '{}'", "user");
    try {
      return pjp.proceed();
    } finally {
      log.info("Clearing security context");
      SecurityContextHolder.clearContext();
    }
  }

}
