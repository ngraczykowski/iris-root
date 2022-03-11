package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;

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

  @Bean
  AlertProcessor newAlertRecordReader() {
    return new AlertProcessor(
        alertInFlightService,
        alertRecordCompositeCollectionReader(),
        alertHandler());
  }

  private AlertCompositeCollectionReader alertRecordCompositeCollectionReader() {
    return new AlertCompositeCollectionReader(
        alertMapper,
        databaseAlertRecordCompositeReader,
        processOnlyUnsolvedAlerts());
  }

  private boolean processOnlyUnsolvedAlerts() {
    return !alertReaderProperties.isSolvedAlertsProcessingEnabled();
  }

  private AlertHandler alertHandler() {
    return new AlertHandler(
        alertInFlightService,
        cbsAckGateway);
  }
}
