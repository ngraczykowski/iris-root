package com.silenteight.payments.bridge.svb.newlearning.step.remove;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.REMOVE_DUPLICATES_STEP;

@Configuration
@RequiredArgsConstructor
public class RemoveDuplicatesTaskletConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final RemoveDuplicatesTasklet removeDuplicatesTasklet;

  @Bean
  Step removeDuplicatesStep() {
    return this.stepBuilderFactory
        .get(REMOVE_DUPLICATES_STEP)
        .tasklet(removeDuplicatesTasklet)
        .build();
  }
}
