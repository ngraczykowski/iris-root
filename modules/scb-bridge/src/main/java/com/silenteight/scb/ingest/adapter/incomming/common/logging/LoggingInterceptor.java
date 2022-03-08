package com.silenteight.scb.ingest.adapter.incomming.common.logging;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logObject;

@Component
@Slf4j
class LoggingInterceptor extends HandlerInterceptorAdapter {

  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex) {

    logObject("url.original", request.getRequestURI());
    logObject("http.request.method", request.getMethod());
    logObject("http.response.status_code", response.getStatus());

    log.info(
        "REST request: method={}, uri={}, params={}, responseCode={}, exception={}",
        request.getMethod(),
        request.getRequestURI(),
        request.getQueryString(),
        response.getStatus(),
        getErrorMessage(request, ex));
  }

  private static String getErrorMessage(HttpServletRequest request, Exception ex) {
    //ex - does not include exceptions that have been handled through an exception resolver
    if (ex != null)
      return ex.getMessage();
    var exception = request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
    return exception instanceof Throwable ? ((Throwable) exception).getMessage() : "";
  }
}
