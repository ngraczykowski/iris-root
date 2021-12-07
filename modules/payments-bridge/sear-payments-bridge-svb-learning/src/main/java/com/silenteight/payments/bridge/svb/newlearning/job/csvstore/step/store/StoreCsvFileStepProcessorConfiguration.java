package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.StoreCsvJobProperties;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StoreCsvJobProperties.class)
@Slf4j
class StoreCsvFileStepProcessorConfiguration {

  @Bean
  @StepScope
  StoreCsvFileStepProcessor storeScvFileStepProcessor(
      @Value("#{stepExecution}") StepExecution stepExecution) {
    var fileName =
        stepExecution.getJobParameters().getString(FILE_NAME_PARAMETER);
    var jobId = stepExecution.getJobExecution().getJobId();
    log.info("Executing step {} for file:{}", stepExecution.getStepName(), fileName);
    return new StoreCsvFileStepProcessor(jobId);
  }
}
