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

import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_REMOVE_PII_JOB_NAME;
import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_REMOVE_PII_STEP_NAME;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DataRetentionProperties.class)
class PiiRetentionJobConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT alert_name FROM pb_alert_data_retention ar\n"
          + "WHERE ar.alert_time < ? AND\n"
          + "    ar.pii_removed_at IS NULL";

  private final JobBuilderFactory jobBuilderFactory;
  private final DataRetentionProperties properties;

  @Bean
  Job sendRemovePiiJob(Step sendRemovePiiStep) {
    return this.jobBuilderFactory.get(SEND_REMOVE_PII_JOB_NAME)
        .start(sendRemovePiiStep)
        .build();
  }

  @Bean
  Step sendRemovePiiStep(
      JdbcCursorItemReader<String> piiAlertsReader,
      ItemWriter<String> piiRetentionWriter,
      StepBuilderFactory stepBuilderFactory) {
    return stepBuilderFactory
        .get(SEND_REMOVE_PII_STEP_NAME)
        .<String, String>chunk(properties.getAlertData().getChunkSize())
        .reader(piiAlertsReader)
        .writer(piiRetentionWriter)
        .build();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<String> piiAlertsReader(DataSource dataSource) {
    var expiration = properties.getPersonalInformation().getExpiration();

    return new JdbcCursorItemReaderBuilder<String>()
        .name("piiAlertsReader")
        .dataSource(dataSource)
        .sql(QUERY)
        .queryArguments(OffsetDateTime.now().minus(expiration))
        .rowMapper(new NameRowMapper())
        .build();
  }
}
