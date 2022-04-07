package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.batch.NameRowMapper;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import javax.sql.DataSource;

import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_ALERTS_EXPIRED_JOB_NAME;
import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_ALERTS_EXPIRED_STEP_NAME;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DataRetentionProperties.class)
class AlertRetentionJobConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT alert_name FROM pb_alert_data_retention ar\n"
          + "WHERE ar.alert_time < ? AND\n"
          + "    ar.alert_data_removed_at IS NULL";

  private final JobBuilderFactory jobBuilderFactory;
  private final DataRetentionProperties properties;

  @Bean
  Job sendAlertsExpiredJob(Step sendAlertsExpiredStep) {
    return this.jobBuilderFactory.get(SEND_ALERTS_EXPIRED_JOB_NAME)
        .start(sendAlertsExpiredStep)
        .build();
  }

  @Bean
  Step sendAlertsExpiredStep(
      JdbcCursorItemReader<String> expiredAlertsReader,
      ItemWriter<String> alertRetentionWriter,
      StepBuilderFactory stepBuilderFactory) {
    return stepBuilderFactory
        .get(SEND_ALERTS_EXPIRED_STEP_NAME)
        .<String, String>chunk(properties.getAlertData().getChunkSize())
        .reader(expiredAlertsReader)
        .writer(alertRetentionWriter)
        .build();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<String> expiredAlertsReader(DataSource dataSource) {
    var expiration = properties.getAlertData().getExpiration();

    return new JdbcCursorItemReaderBuilder<String>()
        .name("expiredAlertsReader")
        .dataSource(dataSource)
        .sql(QUERY)
        .queryArguments(OffsetDateTime.now().minus(expiration))
        .rowMapper(new NameRowMapper())
        .build();
  }
}
