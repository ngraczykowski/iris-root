package com.silenteight.sens.webapp.backend.security;

import com.silenteight.sens.webapp.backend.security.dto.ErrorDto;
import com.silenteight.sens.webapp.backend.security.dto.GenericErrorResponseDto;
import com.silenteight.sens.webapp.backend.support.JsonResponseWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private JsonResponseWriter<GenericErrorResponseDto> responseWriter;

  public RestAccessDeniedHandler(ObjectMapper objectMapper) {
    responseWriter = new JsonResponseWriter<>(objectMapper, GenericErrorResponseDto.class);
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException)
      throws IOException {
    responseWriter.writeResponse(SC_FORBIDDEN, response, createResponse(accessDeniedException));
  }

  private static GenericErrorResponseDto createResponse(
      AccessDeniedException accessDeniedException) {
    return new GenericErrorResponseDto(new ErrorDto(accessDeniedException));
  }
}
