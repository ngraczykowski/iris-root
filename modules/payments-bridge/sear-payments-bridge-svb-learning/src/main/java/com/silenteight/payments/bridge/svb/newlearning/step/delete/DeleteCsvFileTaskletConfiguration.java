package com.silenteight.payments.bridge.svb.newlearning.step.delete;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.StoreCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DeleteCsvFileTaskletConfiguration {

  public static final String DELETE_FILE_STEP = "delete-csv-file";

  private final StepBuilderFactory stepBuilderFactory;
  private final StoreCsvJobProperties properties;
  private final CsvFileResourceProvider resourceProvider;

  @Bean
  Step deleteFileStep() {
    return this.stepBuilderFactory
        .get(DELETE_FILE_STEP)
        .tasklet(deleteFileTasklet())
        .build();
  }

  @Bean
  Tasklet deleteFileTasklet() {
    return new DeleteFileTasklet(resourceProvider, properties.isSkipDeletion());
  }
}
