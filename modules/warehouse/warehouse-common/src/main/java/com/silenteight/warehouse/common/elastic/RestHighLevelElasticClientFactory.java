package com.silenteight.warehouse.common.elastic;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.TerminalClientConfigurationBuilder;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.springframework.data.elasticsearch.client.RestClients.create;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
class RestHighLevelElasticClientFactory {

  private static final Pattern URI_SEPARATOR = compile("://");

  private final RestClientBuilder restClientBuilder;
  private final ElasticsearchRestClientProperties properties;
  private final UserAwareTokenProvider userAwareTokenProvider;

  RestHighLevelClient getAdminClient() {
    return new RestHighLevelClient(restClientBuilder);
  }

  @SuppressWarnings("squid:S2095")
  // ElasticsearchRestClient delegates close to RestHighLevelClient which is a container-managed
  // bean and automatically closed on bean disposal.
  RestHighLevelClient getUserAwareClient() {
    ClientConfiguration clientConfiguration = getBuilderForUris(properties.getUris())
        .withConnectTimeout(properties.getConnectionTimeout())
        .withSocketTimeout(properties.getReadTimeout())
        .withHeaders(this::createUserAwareHeaders)
        .build();

    return create(clientConfiguration).rest();
  }

  private HttpHeaders createUserAwareHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(AUTHORIZATION, userAwareTokenProvider.getAuthorizationToken());
    return httpHeaders;
  }

  private TerminalClientConfigurationBuilder getBuilderForUris(List<String> uris) {
    String[] hostAndPorts = uris.stream()
        .map(RestHighLevelElasticClientFactory::extractUri)
        .toArray(String[]::new);

    boolean usingSsl = properties.getUris().stream()
        .anyMatch(uri -> uri.contains("https"));

    if (usingSsl) {
      return ClientConfiguration.builder()
          .connectedTo(hostAndPorts)
          .usingSsl();
    } else {
      return ClientConfiguration.builder()
          .connectedTo(hostAndPorts);
    }
  }

  private static String extractUri(String uri) {
    return Arrays.stream(URI_SEPARATOR.split(uri))
        .reduce((first, second) -> second)
        .orElseThrow(() -> new IllegalArgumentException("Invalid elasticsearch uri, uri=" + uri));
  }
}
