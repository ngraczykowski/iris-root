package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlAlert;
import com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.step.composite.AlertCompositeReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobConstants.ETL_STEP_NAME;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(EtlJobProperties.class)
class EtlAlertStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY = "SELECT learning_alert_id"
      + " FROM pb_learning_etl_reservation"
      + " WHERE job_id=?";

  private final StepBuilderFactory stepBuilderFactory;
  private final EtlJobProperties properties;
  private final AlertCompositeReaderFactory alertCompositeReaderFactory;

  @Bean
  @StepScope
  public AbstractItemStreamItemReader<AlertComposite> compositeAlertReader(
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return alertCompositeReaderFactory.createAlertCompositeReader(
        QUERY, jobId, properties.getChunkSize());
  }

  @Bean
  @StepScope
  public ItemProcessor<AlertComposite, EtlAlert> etlAlertsCompositeProcessor(
      MarkAlertRegistrationStatusProcessor markAlertRegistrationstatusProcessor) {
    var etlAlertsCompositeProcessor = new CompositeItemProcessor<AlertComposite, EtlAlert>();
    etlAlertsCompositeProcessor.setDelegates(List.of(markAlertRegistrationstatusProcessor));
    return etlAlertsCompositeProcessor;
  }

  @Bean
  Step processUnregisterAlertStep(
      AbstractItemStreamItemReader<AlertComposite> compositeAlertReader,
      ItemProcessor<AlertComposite, EtlAlert> etlAlertsCompositeProcessor) {
    return stepBuilderFactory
        .get(ETL_STEP_NAME)
        .listener(new JobParameterExecutionContextCopyListener())
        .<AlertComposite, EtlAlert>chunk(properties.getChunkSize())
        .reader(compositeAlertReader)
        .processor(etlAlertsCompositeProcessor)
        .writer(item -> log.debug("writing item: {}", item))
        .build();
  }
}
