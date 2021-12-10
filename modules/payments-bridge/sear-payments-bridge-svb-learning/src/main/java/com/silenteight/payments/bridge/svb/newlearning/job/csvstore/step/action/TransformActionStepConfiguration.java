package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.StoreCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.TransformReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.TRANSFORM_ACTION_STEP;

@Configuration
@RequiredArgsConstructor
@Slf4j
class TransformActionStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JpaWriterFactory writerFactory;
  private final StoreCsvJobProperties properties;

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT"
          + " fkco_v_action_comment,\n"
          + "       fkco_action_date,\n"
          + "       fkco_d_action_datetime,\n"
          + "       fkco_operator,\n"
          + "       fkco_status,\n"
          + "       fkco_i_total_action,\n"
          + "       fkco_messages,\n"
          + "       fkco_v_status_name,\n"
          + "       fkco_v_status_behavior\n"
          + "FROM pb_learning_csv_row\n"
          +
          "WHERE job_id"
          + " = ?\n"
          + "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9";

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningActionEntity> actionRowReader(
      TransformReaderFactory readerFactory,
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {

    return readerFactory.createReader(
        jobId, QUERY, new LearningActionRowMapper(properties.getTimeZone()));
  }

  @Bean
  Step transformActionStep(JdbcCursorItemReader<LearningActionEntity> actionRowReader) {
    return stepBuilderFactory
        .get(TRANSFORM_ACTION_STEP)
        .listener(new JobParameterExecutionContextCopyListener())
        .chunk(properties.getChunkSize())
        .reader(actionRowReader)
        .writer(writerFactory.createJpaWriter())
        .build();
  }


}
