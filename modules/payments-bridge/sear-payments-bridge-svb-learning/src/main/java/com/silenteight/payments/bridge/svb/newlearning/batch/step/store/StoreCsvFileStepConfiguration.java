package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.LoadCsvJobProperties;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.STORE_FILE_STEP;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(LoadCsvJobProperties.class)
public class StoreCsvFileStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final LoadCsvJobProperties properties;

  private final FlatFileItemReader<LearningCsvRowEntity> storeCsvFileStepItemReader;
  private final JpaWriterFactory jpaWriterFactory;
  private final ItemProcessor storeCsvFileStepProcessor;

  @SuppressWarnings("unchecked")
  @Bean
  Step storeCsvFileStep() {
    return this.stepBuilderFactory
        .get(STORE_FILE_STEP)
        .<LearningCsvRowEntity, LearningCsvRowEntity>chunk(properties.getChunkSize())
        .reader(storeCsvFileStepItemReader)
        .processor(storeCsvFileStepProcessor)
        .writer(jpaWriterFactory.createJpaWriter())
        .faultTolerant()
        .retryPolicy(new AlwaysRetryPolicy())
        .retry(UnsupportedOperationException.class)
        .backOffPolicy(new FixedBackOffPolicy())
        .build();
  }
}
