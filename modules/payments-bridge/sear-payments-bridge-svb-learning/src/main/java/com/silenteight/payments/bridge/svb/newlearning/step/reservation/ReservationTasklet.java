package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
class ReservationTasklet implements Tasklet {

  private final ReservationQueryExecutor reservationQueryExecutor;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    return RepeatStatus.continueIf(reservationQueryExecutor.reserve() > 0);
  }
}
