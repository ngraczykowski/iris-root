package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.metrics.CbsOracleMetrics;
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertReaderConfiguration {

  private final AlertInFlightService alertInFlightService;
  private final AlertMapper alertMapper;
  private final CbsAckGateway cbsAckGateway;
  private final DatabaseAlertRecordCompositeReader databaseAlertRecordCompositeReader;
  private final AlertReaderProperties alertReaderProperties;
  private final BatchAlertIngestService ingestService;
  private final RawAlertService rawAlertService;
  private final CbsOracleMetrics cbsOracleMetrics;
  private final BatchInfoService batchInfoService;
  private final TrafficManager trafficManager;

  @Bean
  AlertProcessor alertProcessor() {
    return AlertProcessor.builder()
        .alertInFlightService(alertInFlightService)
        .alertHandler(alertHandler())
        .alertCompositeCollectionReader(alertRecordCompositeCollectionReader())
        .batchInfoService(batchInfoService)
        .trafficManager(trafficManager)
        .build();
  }

  private AlertCompositeCollectionReader alertRecordCompositeCollectionReader() {
    return new AlertCompositeCollectionReader(
        alertMapper,
        databaseAlertRecordCompositeReader,
        processOnlyUnsolvedAlerts(),
        cbsOracleMetrics);
  }

  private boolean processOnlyUnsolvedAlerts() {
    return !alertReaderProperties.isSolvedAlertsProcessingEnabled();
  }

  private AlertHandler alertHandler() {
    return AlertHandler.builder()
        .alertInFlightService(alertInFlightService)
        .cbsAckGateway(cbsAckGateway)
        .alertMapper(alertMapper)
        .ingestService(ingestService)
        .rawAlertService(rawAlertService)
        .build();
  }
}
