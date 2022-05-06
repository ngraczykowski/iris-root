package com.silenteight.connector.ftcc.callback.response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.time.Clock;
import javax.validation.Valid;

import static java.nio.file.Files.newInputStream;
import static java.security.KeyStore.getInstance;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.springframework.util.ResourceUtils.getFile;

@Slf4j
@Configuration
@EnableJpaRepositories
@EntityScan
@EnableConfigurationProperties(RecommendationSenderProperties.class)
@RequiredArgsConstructor
class ResponseConfiguration {

  @Valid
  private final RecommendationSenderProperties properties;

  @Bean
  RestTemplate restTemplate(
      RestTemplateBuilder restTemplateBuilder,
      @Valid RecommendationSenderProperties properties) throws Exception {
    log.info("Creating RestTemplate with SSL context...");

    String password = properties.getKeystorePassword();
    String keyStorePath = properties.getKeystorePath();
    SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
    if (isNoneBlank(password, keyStorePath)) {
      sslContextBuilder.loadKeyMaterial(
          keyStore(properties.getKeystorePath(), password.toCharArray()), password.toCharArray());
      log.info(
          "Enable mTLS for RestTemplate for endpoint {} with key {}", properties.getEndpoint(),
          properties.getKeystorePath());
    }

    CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
        .setSSLContext(sslContextBuilder.build())
        .build();

    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);

    return restTemplateBuilder.requestFactory(() -> requestFactory)
        .setReadTimeout(properties.getReadTimeout())
        .setConnectTimeout(properties.getConnectionTimeout())
        .build();
  }

  @Bean
  CallbackRequestService callbackRequestService(
      @NonNull CallbackRequestRepository callbackRequestRepository,
      @NonNull PartitionCreator partitionCreator,
      @NonNull Clock clock) {

    return new CallbackRequestService(callbackRequestRepository, partitionCreator, clock);
  }

  @Bean
  RecommendationSender recommendationSender(
      RestTemplate restTemplate, CallbackRequestService callbackRequestService) {
    return new RecommendationSender(restTemplate, properties.getEndpoint(), callbackRequestService);
  }

  @Bean
  MessageDetailsService messageDetailsService(MessageDetailsQuery messageDetailsQuery) {
    return new MessageDetailsServiceImpl(messageDetailsQuery);
  }

  private static KeyStore keyStore(String file, char[] password) throws Exception {
    KeyStore keyStore = getInstance("PKCS12");
    File key = getFile(file);
    try (InputStream in = newInputStream(key.toPath())) {
      keyStore.load(in, password);
    }
    return keyStore;
  }

}
