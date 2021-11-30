package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

  private final FlatFileItemReader<LearningCsvRowEntity> itemReader;
  private final LearningCsvRowWriter itemWriter;
  private final StoreCsvFileStepListener stepExecutionListener;

  @SuppressWarnings("unchecked")
  @Bean
  Step storeCsvFileStep() {
    return this.stepBuilderFactory
        .get(STORE_FILE_STEP)
        .listener(stepExecutionListener)
        .chunk(properties.getChunkSize())
        .reader(itemReader)
        .writer(itemWriter)
        .build();
  }
}
