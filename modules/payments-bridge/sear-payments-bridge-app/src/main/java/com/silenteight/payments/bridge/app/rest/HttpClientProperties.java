package com.silenteight.payments.bridge.app.rest;

import lombok.Data;

import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.http-client")
class HttpClientProperties {

  @Min(1)
  private int maxConnPerRoute = 64;

  @Min(1)
  private int maxConnTotal = 256;

  private Duration connectionTimeToLive = Duration.ofSeconds(60);

  private boolean evictExpiredConnections = true;

  private Duration maxIdleTime = Duration.ZERO;

  ClientHttpRequestFactory createRequestFactory() {
    var clientBuilder = HttpClients
        .custom()
        .useSystemProperties()
        .setMaxConnPerRoute(maxConnPerRoute)
        .setMaxConnTotal(maxConnTotal);

    if (!connectionTimeToLive.isZero()) {
      clientBuilder.setConnectionTimeToLive(connectionTimeToLive.toMillis(), TimeUnit.MILLISECONDS);
    }

    if (evictExpiredConnections) {
      clientBuilder.evictExpiredConnections();
    }

    if (!maxIdleTime.isZero()) {
      clientBuilder.evictIdleConnections(maxIdleTime.toMillis(), TimeUnit.MILLISECONDS);
    }

    return new HttpComponentsClientHttpRequestFactory(clientBuilder.build());
  }
}
