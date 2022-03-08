package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncService;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class ScbSyncStepsConfiguration {

  private static final String STEP_NAME_PREFIX =
      ScbSyncStepsConfiguration.class.getPackageName() + "#";

  private final StepBuilderFactory stepBuilderFactory;
  private final ScbBridgeConfigProperties properties;
  private final GnsSyncService syncService;

  @Bean
  public Step startNewGnsSync() {
    return createStep("startNewGnsSync")
        .tasklet(new CreateNewGnsSyncTasklet(syncService))
        .listener(executionContextCopyListener())
        .build();
  }

  @Bean
  public Step finishGnsSyncTasklet() {
    return createStep("finishGnsSyncTasklet")
        .tasklet(new FinishGnsSyncTasklet(syncService))
        .listener(executionContextCopyListener())
        .build();
  }

  @Bean
  MeteringAlertCompositeProcessor meteringAlertCompositeProcessor() {
    return new MeteringAlertCompositeProcessor();
  }

  @Bean
  public Step collectRecordsAlertLevel(
      RecordCompositeReader alertLevelRecordCompositeReader,
      RecordCompositeWriter alertLevelRecordCompositeWriter) {
    return createCollectRecordsStep(
        alertLevelRecordCompositeReader, alertLevelRecordCompositeWriter);
  }

  @Bean
  public Step collectRecordsWatchlistLevel(
      RecordCompositeReader watchlistLevelRecordCompositeReader,
      RecordCompositeWriter watchlistLevelRecordCompositeWriter) {
    return createCollectRecordsStep(
        watchlistLevelRecordCompositeReader, watchlistLevelRecordCompositeWriter);
  }

  @Bean
  public Step collectRecordsAlertLevelLearning(
      RecordCompositeReader learningAlertLevelRecordCompositeReader,
      RecordCompositeWriter scbLearningAlertLevelRecordCompositeWriter) {
    return createCollectRecordsStep(
        learningAlertLevelRecordCompositeReader, scbLearningAlertLevelRecordCompositeWriter);
  }

  @Bean
  public Step collectRecordsWatchlistLevelLearning(
      RecordCompositeReader learningWatchlistLevelRecordCompositeReader,
      RecordCompositeWriter scbLearningWatchlistLevelRecordCompositeWriter) {
    return createCollectRecordsStep(
        learningWatchlistLevelRecordCompositeReader,
        scbLearningWatchlistLevelRecordCompositeWriter);
  }

  @Bean
  public JobParameterExecutionContextCopyListener executionContextCopyListener() {
    JobParameterExecutionContextCopyListener listener =
        new JobParameterExecutionContextCopyListener();
    listener.setKeys(new String[] { GnsSyncConstants.GNS_SYNC_MODE_KEY });
    return listener;
  }

  private Step createCollectRecordsStep(
      RecordCompositeReader reader, RecordCompositeWriter writer) {
    return createStep("collectRecordsStep")
        .<AlertComposite, AlertComposite>chunk(properties.getChunkSize())
        .reader(reader)
        .processor(meteringAlertCompositeProcessor())
        .writer(writer)
        .build();
  }

  @NotNull
  private StepBuilder createStep(String stepName) {
    return stepBuilderFactory.get(STEP_NAME_PREFIX + stepName);
  }
}
