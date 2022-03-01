package com.silenteight.payments.bridge.svb.learning.step.retention;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.port.CreateFileRetentionUseCase;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FileDataRetentionTaskletConfiguration {

  public static final String FILE_RETENTION_STEP = "file-retention";

  private final StepBuilderFactory stepBuilderFactory;
  private final CreateFileRetentionUseCase createFileRetentionUseCase;

  @Bean
  Step fileRetentionStep() {
    return this.stepBuilderFactory
        .get(FILE_RETENTION_STEP)
        .tasklet(fileRetentionTasklet())
        .build();
  }

  @Bean
  Tasklet fileRetentionTasklet() {
    return new FileRetentionTasklet(createFileRetentionUseCase);
  }
}
