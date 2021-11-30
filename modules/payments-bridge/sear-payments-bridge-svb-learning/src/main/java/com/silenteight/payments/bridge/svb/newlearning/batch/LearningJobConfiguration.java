package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
class LearningJobConfiguration {

  static final String JOB_NAME = "learning-job";

  private final JobBuilderFactory jobBuilderFactory;
  private final Step storeCsvFileStep;

  @Bean("svbCsvJob")
  Job svbLearningJob() {
    return this.jobBuilderFactory.get(JOB_NAME)
        .start(storeCsvFileStep)
        .build();
  }

  @Bean
  CsvFileResourceProvider csvFileResourceProvider() {
    return new DefaultCsvFileResourceProvider();
  }

}
