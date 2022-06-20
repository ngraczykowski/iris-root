/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.*;

class LoggingInterceptorTest {

  private static final String HTTP_METHOD = "GET";
  private static final String URI = "/test";
  private static final String PARAM = "abc";
  private Logger logger;
  private ListAppender<ILoggingEvent> listAppender;
  private LoggingInterceptor underTest;

  @BeforeEach
  public void setUp() {
    logger = (Logger) LoggerFactory.getLogger(LoggingInterceptor.class);
    listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);
    underTest = new LoggingInterceptor();
  }

  @Test
  void shouldLogSuccessfulRequest() {
    underTest.afterCompletion(mockRequest(), mockResponse(200), null, null);

    var logsList = listAppender.list;
    assertThat(logsList).hasSize(1);
    assertLogEntry(logsList.get(0), 200, EMPTY);
  }

  @Test
  void shouldLogRequestWithError() {
    var errorMsg = "Test message";

    underTest.afterCompletion(
        mockRequest(), mockResponse(503), null, new RuntimeException(errorMsg));

    var logsList = listAppender.list;
    assertThat(logsList).hasSize(1);
    assertLogEntry(logsList.get(0), 503, errorMsg);
  }

  @Test
  void shouldLogRequestWithErrorHandledThroughExceptionResolver() {
    var errorMsg = "Exception handled through an exception resolver";
    var request = mockRequest();
    request.setAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE, new RuntimeException(errorMsg));

    underTest.afterCompletion(request, mockResponse(503), null, null);

    var logsList = listAppender.list;
    assertThat(logsList).hasSize(1);
    assertLogEntry(logsList.get(0), 503, errorMsg);
  }

  @Test
  void shouldLogRequestWithNotHandledError() {
    var errorHandledMsg = "Exception handled through an exception resolver";
    var errorNotHandledMsg = "Exception not handled";
    var request = mockRequest();
    request.setAttribute(
        DispatcherServlet.EXCEPTION_ATTRIBUTE, new RuntimeException(errorHandledMsg));

    underTest.afterCompletion(
        request, mockResponse(503), null, new RuntimeException(errorNotHandledMsg));

    var logsList = listAppender.list;
    assertThat(logsList).hasSize(1);
    assertLogEntry(logsList.get(0), 503, errorNotHandledMsg);
  }

  private void assertLogEntry(ILoggingEvent logEvent, int httpStatus, String errorMsg) {
    assertThat(logEvent.getMessage()).isEqualTo(
        "REST request: method={}, uri={}, params={}, responseCode={}, exception={}");
    var argumentArray = logEvent.getArgumentArray();
    assertThat(argumentArray).containsExactly(
        HTTP_METHOD,
        URI,
        PARAM,
        httpStatus,
        errorMsg
    );
    assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
  }

  private MockHttpServletRequest mockRequest() {
    var request = new MockHttpServletRequest();
    request.setRequestURI(URI);
    request.setQueryString(PARAM);
    request.setMethod(HTTP_METHOD);
    return request;
  }

  private MockHttpServletResponse mockResponse(int httpStatus) {
    var response = new MockHttpServletResponse();
    response.setStatus(httpStatus);
    return response;
  }
}
