package com.silenteight.payments.bridge.svb.newlearning.job;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.port.FindRegisteredAlertPort;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class TestApplicationConfiguration {

  @Bean
  BatchProperties batchProperties() {
    BatchProperties properties = new BatchProperties();
    properties.setTablePrefix("pb_batch_");
    return properties;
  }

  @Bean
  FindRegisteredAlertPort findRegisteredAlertPort() {
    return new FindRegisteredAlertPortMock();
  }

  @Bean
  RegisterAlertUseCase registerAlertUseCase() {
    return new RegisterAlertUseCaseMock();
  }

  @Bean
  MessageParserUseCase messageParserUseCase() {
    return new MessageParserMock();
  }
}
