package com.silenteight.sens.webapp.backend.application.logging;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.logging.SensWebappMdcKeys;

import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.INTERNAL;

@Slf4j
class SensMdcClearingInterceptor extends HandlerInterceptorAdapter {

  @Override
  public void postHandle(
      HttpServletRequest request, 
      HttpServletResponse response, 
      Object handler,
      ModelAndView modelAndView) {
    log.debug(INTERNAL, "Clearing MDC after request");
    SensWebappMdcKeys.getAllKeys().forEach(MDC::remove);
    log.debug(INTERNAL, "MDC cleared");
  }
}
