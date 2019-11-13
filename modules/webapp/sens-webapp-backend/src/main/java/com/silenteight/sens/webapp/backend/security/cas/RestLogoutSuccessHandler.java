package com.silenteight.sens.webapp.backend.security.cas;

import lombok.NonNull;

import com.silenteight.sens.webapp.backend.security.dto.LogoutResponseDto;
import com.silenteight.sens.webapp.backend.support.JsonResponseWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestLogoutSuccessHandler extends AbstractCasSupport implements LogoutSuccessHandler {

  private final String logoutUrl;

  private final JsonResponseWriter<LogoutResponseDto> responseWriter;

  public RestLogoutSuccessHandler(
      ServiceProperties serviceProperties,
      @NonNull String logoutUrl,
      @NonNull ObjectMapper objectMapper) {
    super(serviceProperties);
    this.logoutUrl = logoutUrl;
    responseWriter = new JsonResponseWriter<>(objectMapper, LogoutResponseDto.class);
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) throws IOException {
    String urlEncodedService = createServiceUrl(request, response);
    String casUrl = createCasUrl(this.logoutUrl, urlEncodedService);
    LogoutResponseDto responseDto = createResponse(casUrl);

    responseWriter.writeResponse(HttpServletResponse.SC_OK, response, responseDto);
  }

  private static LogoutResponseDto createResponse(String redirectUrl) {
    return new LogoutResponseDto(redirectUrl);
  }
}
