package com.silenteight.sens.webapp.backend.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;

public class JsonResponseWriter<T> {

  private final ObjectWriter responseWriter;

  public JsonResponseWriter(ObjectMapper objectMapper, Class<T> type) {
    responseWriter = objectMapper.writerFor(type);
  }

  public void writeResponse(int statusCode, HttpServletResponse response, T responseObject)
      throws IOException {
    response.setStatus(statusCode);
    response.setContentType("application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    responseWriter.writeValue(response.getWriter(), responseObject);
    response.flushBuffer();
  }
}
