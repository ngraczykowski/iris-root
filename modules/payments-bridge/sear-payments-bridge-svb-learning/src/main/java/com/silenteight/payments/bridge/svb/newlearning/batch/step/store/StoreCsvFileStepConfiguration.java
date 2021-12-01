package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.JpaWriterFactory;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StoreCsvFileProperties.class)
public class StoreCsvFileStepConfiguration {

  public static final String STORE_FILE_STEP = "store-csv-file";

  private final StepBuilderFactory stepBuilderFactory;
  private final StoreCsvFileProperties properties;

  private final FlatFileItemReader<LearningCsvRowEntity> storeCsvFileStepItemReader;
  private final JpaWriterFactory jpaWriterFactory;
  private final ItemProcessor storeCsvFileStepProcessor;
  private final StepExecutionListener storeCsvFileStepListener;

  @SuppressWarnings("unchecked")
  @Bean
  Step storeCsvFileStep() {
    return this.stepBuilderFactory
        .get(STORE_FILE_STEP)
        .listener(storeCsvFileStepListener)
        .chunk(properties.getChunkSize())
        .reader(storeCsvFileStepItemReader)
        .processor(storeCsvFileStepProcessor)
        .writer(jpaWriterFactory.createJpaWriter())
        .build();
  }
}
