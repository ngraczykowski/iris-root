package com.silenteight.payments.bridge.app.batch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BatchProcessingProperties.class)
public class BatchProcessingConfiguration {

  private final BatchProcessingProperties batchProcessingProperties;

  @Primary
  @SneakyThrows
  @Bean
  public JobLauncher searJobLauncher(final JobRepository jobRepository) {
    final SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
    taskExecutor.setConcurrencyLimit(batchProcessingProperties.getConcurrencyLimit());
    final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(taskExecutor);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }
}
