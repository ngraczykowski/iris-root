package com.silenteight.warehouse.common.opendistro.kibana;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpendistroTestConfiguration {

  @Bean
  OpendistroKibanaTestClient opendistroKibanaTestClient(
      OpendistroKibanaClient opendistroKibanaClient) {
    return new OpendistroKibanaTestClient(opendistroKibanaClient);
  }
}
