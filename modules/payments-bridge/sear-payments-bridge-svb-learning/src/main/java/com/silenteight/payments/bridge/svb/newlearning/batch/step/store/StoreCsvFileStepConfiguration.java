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

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(LoadCsvJobProperties.class)
public class StoreCsvFileStepConfiguration {

  public static final String STORE_FILE_STEP = "store-csv-file";

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
        .chunk(properties.getChunkSize())
        .reader(storeCsvFileStepItemReader)
        .processor(storeCsvFileStepProcessor)
        .writer(jpaWriterFactory.createJpaWriter())
        .build();
  }
}
