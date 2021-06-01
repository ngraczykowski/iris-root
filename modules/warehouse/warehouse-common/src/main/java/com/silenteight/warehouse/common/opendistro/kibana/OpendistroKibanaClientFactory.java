package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.util.function.Supplier;
import javax.validation.Valid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
import static java.lang.String.join;
import static java.net.http.HttpClient.Redirect.NORMAL;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.time.Duration.ofSeconds;
import static java.util.Base64.getEncoder;

@RequiredArgsConstructor
public class OpendistroKibanaClientFactory {

  @Valid
  @NonNull
  private final KibanaProperties kibanaProperties;

  @NonNull
  private final UserAwareTokenProvider userAwareTokenProvider;

  public OpendistroKibanaClient getAdminClient() {
    return new OpendistroKibanaClient(
        getHttpClient(),
        kibanaProperties.getUrl(),
        getObjectMapper(),
        getAdminHeadersSupplier(),
        kibanaProperties.getTimezone());
  }

  public OpendistroKibanaClient getUserAwareClient() {
    return new OpendistroKibanaClient(
        getHttpClient(),
        kibanaProperties.getUrl(),
        getObjectMapper(),
        getUserAwareHeadersSupplier(),
        kibanaProperties.getTimezone());
  }

  private static ObjectMapper getObjectMapper() {
    return new ObjectMapper()
        .setPropertyNamingStrategy(SNAKE_CASE)
        .setSerializationInclusion(NON_EMPTY)
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new JavaTimeModule());
  }

  private static HttpClient getHttpClient() {
    return HttpClient.newBuilder()
        .version(HTTP_1_1)
        .followRedirects(NORMAL)
        .connectTimeout(ofSeconds(20))
        .build();
  }

  private Supplier<Builder> getAdminHeadersSupplier() {
    String adminAuthorizationToken = getAdminAuthorizationToken();
    return () -> HttpRequest.newBuilder()
        .header("Authorization", adminAuthorizationToken)
        .header("kbn-xsrf", "true")
        .header("Origin", kibanaProperties.getUrl())
        .timeout(ofSeconds(30));
  }

  private Supplier<Builder> getUserAwareHeadersSupplier() {
    return () -> HttpRequest.newBuilder()
        .header("Authorization", userAwareTokenProvider.getAuthorizationToken())
        .header("kbn-xsrf", "true")
        .header("Origin", kibanaProperties.getUrl())
        .timeout(ofSeconds(30));
  }

  private String getAdminAuthorizationToken() {
    String username = kibanaProperties.getUsername();
    String password = kibanaProperties.getPassword();

    String plain = join(":", username, password);
    String base64 = getEncoder().encodeToString(plain.getBytes());

    return join(" ", "Basic", base64);
  }
}
