package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.alertmapper.AlertMapper;
import com.silenteight.customerbridge.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.customerbridge.cbs.gateway.CbsAckGateway;
import com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfoService;
import com.silenteight.sep.base.common.messaging.MessageSenderFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_ALERT_UNPROCESSED;

@Configuration
@RequiredArgsConstructor
class AlertReaderConfiguration {

  private final AlertInFlightService alertInFlightService;
  private final AlertMapper alertMapper;
  private final CbsAckGateway cbsAckGateway;
  private final DatabaseAlertRecordCompositeReader databaseAlertRecordCompositeReader;
  private final MessageSenderFactory messageSenderFactory;
  private final AlertInfoService alertInfoService;
  private final AlertReaderProperties alertReaderProperties;

  @Bean
  AlertProcessor newAlertRecordReader() {
    return new AlertProcessor(alertInFlightService,
        alertRecordCompositeCollectionReader(),
        alertHandler());
  }

  private AlertCompositeCollectionReader alertRecordCompositeCollectionReader() {
    return new AlertCompositeCollectionReader(
        alertMapper,
        databaseAlertRecordCompositeReader,
        processOnlyUnsolvedAlerts());
  }

  private AlertHandler alertHandler() {
    return new AlertHandler(
        alertInfoService,
        alertInFlightService,
        cbsAckGateway,
        messageSenderFactory.get(EXCHANGE_ALERT_UNPROCESSED));
  }

  private boolean processOnlyUnsolvedAlerts() {
    return !alertReaderProperties.isSolvedAlertsProcessingEnabled();
  }
}
