package com.silenteight.warehouse.common.opendistro.kibana;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.util.function.Supplier;
import javax.validation.Valid;

import static java.net.http.HttpClient.Redirect.NORMAL;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.time.Duration.ofSeconds;
import static java.util.Base64.getEncoder;

@Configuration
@EnableConfigurationProperties(KibanaProperties.class)
class OpendistroKibanaConfiguration {

  @Bean
  OpendistroKibanaClient opendistroKibanaClient(
      @Valid KibanaProperties kibanaProperties, ObjectMapper objectMapper) {

    String username = kibanaProperties.getUsername();
    String password = kibanaProperties.getPassword();
    String kibanaUrl = kibanaProperties.getUrl();
    String timezone = kibanaProperties.getTimezone();

    Supplier<Builder> defaultRequestBuilderProvider = () -> HttpRequest.newBuilder()
        .header("Authorization", basicAuth(username, password))
        .header("kbn-xsrf", "true")
        .header("Origin", kibanaUrl)
        .timeout(ofSeconds(30));

    HttpClient httpClient = HttpClient.newBuilder()
        .version(HTTP_1_1)
        .followRedirects(NORMAL)
        .connectTimeout(ofSeconds(20))
        .build();

    return new OpendistroKibanaClient(
        httpClient, kibanaUrl, objectMapper, defaultRequestBuilderProvider, timezone);
  }

  private static String basicAuth(String username, String password) {
    return "Basic " + getEncoder().encodeToString((username + ":" + password).getBytes());
  }
}
