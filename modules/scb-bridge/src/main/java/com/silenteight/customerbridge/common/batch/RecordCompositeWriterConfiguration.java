package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.gateway.CbsAckGateway;
import com.silenteight.customerbridge.common.batch.RecordCompositeWriter.WriterConfiguration;
import com.silenteight.customerbridge.common.domain.GnsSyncDeltaService;
import com.silenteight.customerbridge.common.ingest.BatchAlertIngestService;
import com.silenteight.customerbridge.common.quartz.*;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecordCompositeWriterConfiguration {

  private final ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingJobProperties;
  private final ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;

  @Bean
  @JobScope
  RecordCompositeWriter alertLevelRecordCompositeWriter(
      CbsAckGateway cbsAckGateway,
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {

    WriterConfiguration writerConfiguration = solvingWriterConfiguration(
        alertLevelSolvingJobProperties.isAckRecords());

    return createRecordCompositeWriter(
        cbsAckGateway, writerConfiguration, deltaService, ingestService,
        alertLevelSolvingJobProperties.getName());
  }

  @Bean
  @JobScope
  RecordCompositeWriter watchlistLevelRecordCompositeWriter(
      CbsAckGateway cbsAckGateway,
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {
    WriterConfiguration writerConfiguration = solvingWriterConfiguration(
        watchlistLevelSolvingJobProperties.isAckRecords());

    return createRecordCompositeWriter(
        cbsAckGateway, writerConfiguration, deltaService, ingestService,
        watchlistLevelSolvingJobProperties.getName());
  }

  @Bean
  @JobScope
  RecordCompositeWriter scbLearningAlertLevelRecordCompositeWriter(
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {

    final WriterConfiguration learningWriterConfiguration =
        WriterConfiguration.of(alertLevelLearningJobProperties.isUseDelta(), false, true);

    return RecordCompositeWriter.builder()
        .configuration(learningWriterConfiguration)
        .deltaService(deltaService)
        .ingestService(ingestService)
        .deltaJobName(alertLevelLearningJobProperties.getDeltaJobName())
        .build();
  }

  @Bean
  @JobScope
  RecordCompositeWriter scbLearningWatchlistLevelRecordCompositeWriter(
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {

    final WriterConfiguration learningWriterConfiguration =
        WriterConfiguration.of(watchlistLevelLearningJobProperties.isUseDelta(), false, true);

    return RecordCompositeWriter.builder()
        .configuration(learningWriterConfiguration)
        .deltaService(deltaService)
        .ingestService(ingestService)
        .deltaJobName(watchlistLevelLearningJobProperties.getDeltaJobName())
        .build();
  }

  @Bean
  @JobScope
  RecordCompositeWriter ecmLearningRecordCompositeWriter(
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {

    final WriterConfiguration learningWriterConfiguration =
        WriterConfiguration.of(ecmBridgeLearningJobProperties.isUseDelta(), false, true);

    return RecordCompositeWriter.builder()
        .configuration(learningWriterConfiguration)
        .deltaService(deltaService)
        .ingestService(ingestService)
        .deltaJobName(ecmBridgeLearningJobProperties.getDeltaJobName())
        .build();
  }

  private static RecordCompositeWriter createRecordCompositeWriter(
      CbsAckGateway cbsAckGateway,
      WriterConfiguration writerConfiguration,
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService,
      String deltaJobName) {
    return RecordCompositeWriter.builder()
        .cbsAckGateway(cbsAckGateway)
        .configuration(writerConfiguration)
        .deltaService(deltaService)
        .ingestService(ingestService)
        .deltaJobName(deltaJobName)
        .build();
  }

  private static WriterConfiguration solvingWriterConfiguration(boolean ackRecords) {
    return WriterConfiguration.of(false, ackRecords, false);
  }
}
