package com.silenteight.warehouse.common.opendistro.kibana;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.LOWER_CAMEL_CASE;

@Configuration
@EnableConfigurationProperties(KibanaProperties.class)
class OpendistroKibanaConfiguration {

  @Bean
  OpendistroKibanaClientFactory opendistroKibanaClientFactory(
      @Valid KibanaProperties kibanaProperties,
      UserAwareTokenProvider userAwareTokenProvider) {

    return new OpendistroKibanaClientFactory(kibanaProperties, userAwareTokenProvider);
  }

  @Bean
  OpendistroReportLoader opendistroReportLoader(
      OpendistroKibanaClientFactory opendistroKibanaClientFactory) {

    return new OpendistroReportLoader(getObjectMapper(),
        opendistroKibanaClientFactory.getAdminClient());
  }

  @Bean
  OpendistroSavedObjectsLoader opendistroSavedObjectsLoader(
      @Valid KibanaProperties kibanaProperties,
      CloseableHttpClient closeableHttpClient) {

    return new OpendistroSavedObjectsLoader(
        kibanaProperties, closeableHttpClient, getObjectMapper());
  }

  @Bean
  CloseableHttpClient closeableHttpClient() {
    return HttpClients.createDefault();
  }

  private static ObjectMapper getObjectMapper() {
    return new ObjectMapper()
        .setPropertyNamingStrategy(LOWER_CAMEL_CASE)
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
