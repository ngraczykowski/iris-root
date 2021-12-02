package com.silenteight.payments.bridge.svb.newlearning.batch.step.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.LoadCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.TransformReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.batch.step.LearningJobConstants.STEP_TRANSFORM_ALERTED_MESSAGE;

@Configuration
@RequiredArgsConstructor
@Slf4j
class TransformAlertedMessageStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JpaWriterFactory writerFactory;
  private final LoadCsvJobProperties properties;

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT"
          + " fkco_messages,\n"
          + "       fkco_d_filtered_datetime,\n"
          + "       fkco_i_blockinghits\n"
          + "FROM pb_learning_csv_row \n"
          + "WHERE job_id = ?\n"
          + "GROUP BY 1, 2, 3;";

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningAlertedMessageEntity> alertedMessageReader(
      TransformReaderFactory readerFactory,
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return readerFactory.createReader(
        jobId, QUERY, new LearningAlertedMessageRowMapper(properties.getTimeZone()));
  }

  @Bean
  Step transformAlertedMessageStep(
      JdbcCursorItemReader<LearningAlertedMessageEntity> alertedMessageReader) {
    return stepBuilderFactory
        .get(STEP_TRANSFORM_ALERTED_MESSAGE)
        .chunk(properties.getChunkSize())
        .reader(alertedMessageReader)
        .writer(writerFactory.createJpaWriter())
        .build();
  }
}
