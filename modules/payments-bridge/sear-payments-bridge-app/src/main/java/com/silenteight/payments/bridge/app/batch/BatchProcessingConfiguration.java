package com.silenteight.payments.bridge.app.batch;

import lombok.SneakyThrows;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@EnableBatchProcessing
@Configuration
public class BatchProcessingConfiguration {


  @Primary
  @SneakyThrows
  @Bean
  public JobLauncher searJobLauncher(final JobRepository jobRepository) {
    final SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
    final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(taskExecutor);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }
}
