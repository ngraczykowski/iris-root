package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;

@Configuration
@RequiredArgsConstructor
class HistoricalAssessmentReservationTaskletConfiguration {

  private final JdbcTemplate jdbcTemplate;
  @Language("PostgreSQL")
  private static final String INSERT_QUERY = "INSERT INTO pb_learning_historical_reservation"
      + " (SELECT a.learning_alert_id, ? FROM pb_learning_alert a"
      + " LEFT JOIN pb_learning_historical_reservation r "
      + " ON a.learning_alert_id = r.learning_alert_id and r.job_id = ?"
      + " WHERE a.file_name = ? and (r.job_id != ? or r.job_id is null))";

  @Bean
  @StepScope
  Tasklet historicalAssessmentReservationTasklet(
      @Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
    var fileName = jobExecution.getJobParameters().getString(FILE_NAME_PARAMETER);
    return new ReservationTasklet(new ReservationQueryExecutor(jdbcTemplate, INSERT_QUERY,
        jobExecution.getJobId(), fileName));
  }

}
