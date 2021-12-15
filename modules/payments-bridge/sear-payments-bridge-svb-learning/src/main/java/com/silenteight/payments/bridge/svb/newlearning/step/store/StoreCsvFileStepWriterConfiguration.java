
package com.silenteight.payments.bridge.svb.newlearning.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.step.StoreCsvJobProperties;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StoreCsvJobProperties.class)
@Slf4j
class StoreCsvFileStepWriterConfiguration {

  private final EntityManagerFactory entityManagerFactory;

  @SuppressWarnings("SpringElInspection")
  @Bean
  @StepScope
  LearningCsvRowWriter<LearningCsvRowEntity> storeCsvFileStepWriter() {
    var writer = new LearningCsvRowWriter<LearningCsvRowEntity>();
    writer.setEntityManagerFactory(entityManagerFactory);
    writer.setUsePersist(true);
    return writer;
  }


}
