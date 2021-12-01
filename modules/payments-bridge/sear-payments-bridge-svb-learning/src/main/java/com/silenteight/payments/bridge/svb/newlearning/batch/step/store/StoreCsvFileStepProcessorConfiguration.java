package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobParameters.FILE_ID_PARAMETER;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StoreCsvFileProperties.class)
@Slf4j
class StoreCsvFileStepProcessorConfiguration {

  @Bean
  @StepScope
  StoreCsvFileStepProcessor storeScvFileStepProcessor(
      @Value("#{stepExecution}") StepExecution stepExecution) {
    var fileId =
        stepExecution.getJobParameters().getLong(FILE_ID_PARAMETER);
    var jobId = stepExecution.getJobExecution().getJobId();
    log.info("Executing step {} for file:{}", stepExecution.getStepName(), fileId);
    return new StoreCsvFileStepProcessor(fileId, jobId);
  }
}
