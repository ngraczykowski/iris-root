package com.silenteight.sens.webapp.backend.security.cas;

import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.security.dto.ErrorDto;
import com.silenteight.sens.webapp.backend.security.dto.UnauthorizedResponseDto;
import com.silenteight.sens.webapp.backend.support.JsonResponseWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.silenteight.commons.ArgumentChecker.requireNotBlank;

@Data
public class RestCasAuthenticationEntryPoint extends AbstractCasSupport
    implements AuthenticationEntryPoint {

  private final String loginUrl;

  private final JsonResponseWriter<UnauthorizedResponseDto> responseWriter;

  public RestCasAuthenticationEntryPoint(ServiceProperties serviceProperties,
                                         @NonNull String loginUrl,
                                         @NonNull ObjectMapper objectMapper) {
    super(serviceProperties);
    this.loginUrl = requireNotBlank(loginUrl, "loginUrl");
    responseWriter = new JsonResponseWriter<>(objectMapper, UnauthorizedResponseDto.class);
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    String urlEncodedService = createServiceUrl(request, response);
    String casUrl = createCasUrl(this.loginUrl, urlEncodedService);
    UnauthorizedResponseDto responseDto = createResponse(authException, casUrl);

    responseWriter.writeResponse(HttpServletResponse.SC_UNAUTHORIZED, response, responseDto);
  }

  private static UnauthorizedResponseDto createResponse(
      AuthenticationException authException, String casUrl) {
    ErrorDto error = new ErrorDto(authException);
    return new UnauthorizedResponseDto(casUrl, error);
  }
}
