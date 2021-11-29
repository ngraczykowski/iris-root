package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.silenteight.payments.bridge.svb.newlearning")
public class TestApplicationConfiguration {

  @Bean
  BatchProperties batchProperties() {
    BatchProperties properties = new BatchProperties();
    properties.setTablePrefix("pb_batch_");
    return properties;
  }
}
