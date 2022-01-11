package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class EtlAlertProcessorConfiguration {

  private final FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  private final ProcessRegisteredService processRegisteredService;
  private final ProcessUnregisteredService processUnregisteredService;

  @Bean
  @StepScope
  EtlAlertProcessor registerAlertProcessor(
      @Value("#{stepExecution}") StepExecution stepExecution) {
    return new EtlAlertProcessor(
        findRegisteredAlertUseCase, processRegisteredService, processUnregisteredService,
        stepExecution.getJobExecutionId());
  }
}
