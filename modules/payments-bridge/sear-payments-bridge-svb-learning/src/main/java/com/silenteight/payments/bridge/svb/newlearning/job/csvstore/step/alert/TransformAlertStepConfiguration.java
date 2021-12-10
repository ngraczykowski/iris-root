package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.StoreCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.TransformReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STEP_TRANSFORM_ALERT;

@Configuration
@RequiredArgsConstructor
@Slf4j
class TransformAlertStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JpaWriterFactory writerFactory;
  private final StoreCsvJobProperties properties;

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT \n"
          + "       fkco_id,\n"
          + "       fkco_v_system_id,\n"
          + "       fkco_v_format,\n"
          + "       fkco_v_type,\n"
          + "       fkco_v_transaction_ref,\n"
          + "       fkco_v_related_ref,\n"
          + "       fkco_v_sens,\n"
          + "       fkco_v_business_unit,\n"
          + "       fkco_v_application,\n"
          + "       fkco_v_currency,\n"
          + "       fkco_f_amount,\n"
          + "       fkco_v_content,\n"
          + "       fkco_b_highlight_all,\n"
          + "       fkco_v_value_date,\n"
          + "       fkco_unit,\n"
          + "       fkco_i_msg_fml_priority,\n"
          + "       fkco_i_msg_fml_confidentiality,\n"
          + "       fkco_d_app_deadline,\n"
          + "       fkco_i_app_priority,\n"
          + "       fkco_i_normamount,\n"
          + "       fkco_v_messageid,\n"
          + "       fkco_v_copy_service,\n"
          + "       fkco_d_filtered_datetime,\n"
          + "       fkco_i_blockinghits\n"
          +
          "FROM pb_learning_csv_row\n"
          + "WHERE job_id = ?\n"
          + "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,"
          + " 22, 23, 24";

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningAlertEntity> alertReader(
      TransformReaderFactory readerFactory,
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return readerFactory.createReader(
        jobId, QUERY, new LearningAlertRowMapper(properties.getTimeZone()));
  }

  @Bean
  Step transformAlertStep(JdbcCursorItemReader<LearningAlertEntity> alertReader) {
    return stepBuilderFactory
        .get(STEP_TRANSFORM_ALERT)
        .chunk(properties.getChunkSize())
        .reader(alertReader)
        .writer(writerFactory.createJpaWriter())
        .build();
  }
}
