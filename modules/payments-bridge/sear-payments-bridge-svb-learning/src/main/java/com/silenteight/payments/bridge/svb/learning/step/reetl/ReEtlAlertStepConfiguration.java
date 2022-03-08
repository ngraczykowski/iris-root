package com.silenteight.payments.bridge.svb.learning.step.reetl;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.job.etl.EtlJobProperties;
import com.silenteight.payments.bridge.svb.learning.step.composite.AlertCompositeReaderFactory;
import com.silenteight.payments.bridge.svb.learning.step.etl.IngestDatasourceService;
import com.silenteight.payments.bridge.svb.learning.step.etl.feature.port.CreateFeatureUseCase;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.learning.job.reetl.ReEtlJobConstants.RE_ETL_STEP_NAME;

@Configuration
@Slf4j
@EnableConfigurationProperties(EtlJobProperties.class)
class ReEtlAlertStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY = "SELECT learning_alert_id"
      + " FROM pb_learning_alert"
      + " WHERE file_name= ? ";


  @Bean
  @StepScope
  AbstractItemStreamItemReader<AlertComposite> reCompositeAlertReader(
      final AlertCompositeReaderFactory alertCompositeReaderFactory,
      @Value("#{stepExecution}") final StepExecution stepExecution,
      final EtlJobProperties properties
  ) {
    final String fileName = stepExecution.getJobParameters().getString(FILE_NAME_PARAMETER);

    return alertCompositeReaderFactory.createAlertCompositeReader(
        QUERY, fileName, properties.getChunkSize());
  }


  @Bean
  Step readCompositeAlertStep(
      final StepBuilderFactory stepBuilderFactory,
      final ReEtlAlertProcessor reEtlAlertProcessor,
      final AbstractItemStreamItemReader<AlertComposite> reCompositeAlertReader,
      final EtlJobProperties properties

  ) {
    return stepBuilderFactory.get(RE_ETL_STEP_NAME)
        .listener(new JobParameterExecutionContextCopyListener())
        .<AlertComposite, Void>chunk(properties.getChunkSize())
        .reader(reCompositeAlertReader)
        .processor(reEtlAlertProcessor)
        .writer(new DummyWriter())
        .faultTolerant()
        .retryPolicy(new AlwaysRetryPolicy())
        .retry(IOException.class)
        .retryLimit(properties.getRetryLimit())
        .backOffPolicy(this.backoffPolicy(properties))
        .skipPolicy(new AlwaysSkipItemSkipPolicy())
        .build();
  }

  @Bean
  @StepScope
  ReEtlAlertProcessor reEtlAlertProcessor(
      final FindRegisteredAlertUseCase findRegisteredAlertUseCase,
      @Value("#{stepExecution}") StepExecution stepExecution,
      final CreateFeatureUseCase createFeatureUseCase,
      final IngestDatasourceService ingestDatasourceService
  ) {
    return create(findRegisteredAlertUseCase, createFeatureUseCase,
        stepExecution.getJobParameters(), ingestDatasourceService
    );
  }

  @Nonnull
  private ReEtlAlertProcessor create(
      final FindRegisteredAlertUseCase findRegisteredAlertUseCase,
      final CreateFeatureUseCase createFeatureUseCase,
      final JobParameters jobParameters,
      final IngestDatasourceService ingestDatasourceService
  ) {

    final List<String> determineFeatureInputList =
        JobParameterTransformer.determineFeatureInputList(jobParameters);
    return ReEtlAlertProcessorFactory.create(findRegisteredAlertUseCase,
        determineFeatureInputList, ingestDatasourceService);
  }

  private BackOffPolicy backoffPolicy(final EtlJobProperties properties) {
    FixedBackOffPolicy policy = new FixedBackOffPolicy();
    policy.setBackOffPeriod(properties.getRetryPeriodMilliseconds());
    return policy;
  }

}
