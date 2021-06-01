package com.silenteight.warehouse.common.opendistro.kibana;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpendistroTestConfiguration {

  @Bean
  OpendistroKibanaTestClient opendistroKibanaTestClient(
      OpendistroKibanaClientFactory opendistroKibanaClientFactory) {
    return new OpendistroKibanaTestClient(opendistroKibanaClientFactory.getAdminClient());
  }
}
