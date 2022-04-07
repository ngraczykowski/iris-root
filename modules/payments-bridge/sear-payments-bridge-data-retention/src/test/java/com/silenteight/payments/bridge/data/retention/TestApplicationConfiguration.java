package com.silenteight.payments.bridge.data.retention;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.port.SendAlertsExpiredPort;
import com.silenteight.payments.bridge.data.retention.port.SendPersonalInformationExpiredPort;

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
  SendAlertsExpiredPort sendAlertsExpiredPort() {
    return new SendAlertExpiredPortMock();
  }

  @Bean
  SendPersonalInformationExpiredPort sendPersonalInformationExpiredPort() {
    return new SendPersonalInformationExpiredMock();
  }
}
