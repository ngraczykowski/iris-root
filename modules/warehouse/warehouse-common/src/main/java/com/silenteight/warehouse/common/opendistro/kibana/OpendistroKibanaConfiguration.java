package com.silenteight.warehouse.common.opendistro.kibana;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(KibanaProperties.class)
class OpendistroKibanaConfiguration {

  @Bean
  OpendistroKibanaClientFactory opendistroKibanaClientFactory(
      @Valid KibanaProperties kibanaProperties,
      UserAwareTokenProvider userAwareTokenProvider) {

    return new OpendistroKibanaClientFactory(kibanaProperties, userAwareTokenProvider);
  }
}
