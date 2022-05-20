package com.silenteight.hsbc.bridge.common.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class RestParametersInterceptor implements HandlerInterceptor {

  private final ApplicationContext context;

  private static final String SPRING_ERROR_ENDPOINT = "/error";
  private static final String TRACE_ID_ATTRIBUTE = "org.springframework.cloud.sleuth.TraceContext";

  @Override
  public boolean preHandle(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler) {

    var requestURI = request.getRequestURI();
    var traceId = extractTraceId(request);
    log.info("REST request received for url: {}, with trace id: {}", requestURI, traceId);

    if (isHttpRequestPolluted(request) && !SPRING_ERROR_ENDPOINT.equals(requestURI)) {
      log.warn("Received polluted parameters for url: {} with trace id: {}", requestURI, traceId);
      checkPathAndSetStatus(request, response);
      return false;
    } else {
      return !SPRING_ERROR_ENDPOINT.equals(requestURI);
    }
  }

  @Override
  public void afterCompletion(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler,
      Exception ex) {

    log.info(
        "REST response sent for url: {}, with trace id: {}",
        request.getRequestURI(),
        extractTraceId(request));
  }

  private String extractTraceId(HttpServletRequest request) {
    return Optional.ofNullable(request.getAttribute(TRACE_ID_ATTRIBUTE))
        .map(Object::toString)
        .orElse("unknown");
  }

  private boolean isHttpRequestPolluted(HttpServletRequest request) {
    return request
        .getParameterMap()
        .entrySet()
        .stream()
        .anyMatch(entry -> entry.getValue().length > 1);
  }

  private void checkPathAndSetStatus(
      HttpServletRequest request,
      HttpServletResponse response) {

    String requestURI = request.getRequestURI();

    var handlerMapping = context.getBean(RequestMappingHandlerMapping.class);

    Set<RequestMethod> restMethods = handlerMapping.getHandlerMethods()
        .keySet()
        .stream()
        .filter($ -> $.getPatternValues().contains(requestURI))
        .flatMap($ -> $.getMethodsCondition().getMethods().stream())
        .collect(Collectors.toSet());

    if (restMethods.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else if (isMethodNotAllowed(restMethods, request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  private boolean isMethodNotAllowed(Set<RequestMethod> restMethods, String method) {
    return restMethods.stream()
        .filter($ -> $.name().equals(method))
        .findAny()
        .isEmpty();
  }
}
