package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.silenteight.payments.bridge.svb.newlearning.step.unregistered.UnregisteredJobConstants.UNREGISTERED_STEP_NAME;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(UnregisteredJobProperties.class)
class ProcessUnregisteredAlertStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY = "SELECT fkco_id FROM pb_learning_alert;";

  private final StepBuilderFactory stepBuilderFactory;
  private final UnregisteredJobProperties properties;
  private final AlertCompositeFetcher alertCompositeFetcher;

  @Bean
  @StepScope
  public AlertCompositeReader compositeAlertReader(DataSource dataSource) {
    var cursorReader = new BetterJdbcCursorItemReader<Long>();
    cursorReader.setSql(QUERY);
    cursorReader.setRowMapper((rs, rowNum) -> rs.getLong(1));
    cursorReader.setDataSource(dataSource);
    return new AlertCompositeReader(alertCompositeFetcher, cursorReader, properties.getChunkSize());
  }

  @Bean
  Step processUnregisterAlertStep(AlertCompositeReader compositeAlertReader) {
    return stepBuilderFactory
        .get(UNREGISTERED_STEP_NAME)
        .listener(new JobParameterExecutionContextCopyListener())
        .chunk(properties.getChunkSize())
        .reader(compositeAlertReader)
        .writer(item -> log.debug("writing item: {}", item))
        .build();
  }
}
