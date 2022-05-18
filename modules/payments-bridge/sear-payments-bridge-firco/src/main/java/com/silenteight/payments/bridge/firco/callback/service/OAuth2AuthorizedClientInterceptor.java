package com.silenteight.payments.bridge.firco.callback.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

@Builder
@Slf4j
class OAuth2AuthorizedClientInterceptor implements ClientHttpRequestInterceptor {

  private final OAuth2AuthorizedClientManager manager;
  private final String clientRegistrationId;
  private final Supplier<Authentication> principalSupplier;

  @Nonnull
  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {

    var authorizedRequest = OAuth2AuthorizeRequest
        .withClientRegistrationId(clientRegistrationId)
        .principal(principalSupplier.get())
        .build();

    var authorizedClient = manager.authorize(authorizedRequest);

    if (authorizedClient != null) {
      var headers = request.getHeaders();
      headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
    } else {
      log.warn("No OAuth2 authorization configured!");
    }

    return execution.execute(request, body);
  }
}
