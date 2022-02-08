package com.silenteight.payments.bridge.svb.newlearning.step.clear;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.REMOVE_DUPLICATES_STEP;

@Configuration
@RequiredArgsConstructor
public class ClearDuplicatesTaskletConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final Tasklet clearDuplicatesTasklet;

  @Bean
  Step clearDuplicatesStep() {
    return this.stepBuilderFactory
        .get(REMOVE_DUPLICATES_STEP)
        .tasklet(clearDuplicatesTasklet)
        .build();
  }
}
