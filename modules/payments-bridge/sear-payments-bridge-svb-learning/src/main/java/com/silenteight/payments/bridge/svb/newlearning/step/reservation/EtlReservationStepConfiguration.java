package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobConstants.ETL_RESERVATION_STEP;

@Configuration
@RequiredArgsConstructor
class EtlReservationStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final Tasklet etlReservationTasklet;

  @Bean
  Step etlReservationStep() {
    return stepBuilderFactory
        .get(ETL_RESERVATION_STEP)
        .tasklet(etlReservationTasklet)
        .build();
  }
}
