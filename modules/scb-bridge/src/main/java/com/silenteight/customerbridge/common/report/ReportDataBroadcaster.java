package com.silenteight.customerbridge.common.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.ingest.IngestServiceListener;
import com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfoMapResult;
import com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfoMapper;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.sep.base.common.messaging.MessageSender;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ROUTE_INFO_FROM_SCB_BRIDGE;
import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ROUTE_SCB_INFO_FROM_SYNC;

@Slf4j
@RequiredArgsConstructor
class ReportDataBroadcaster implements IngestServiceListener {

  private final MessageSender messageSender;
  private final AlertInfoMapper infoMapper;

  public void send(Alert alert) {
    AlertInfoMapResult alertInfoMapResult = infoMapper.map(alert);

    if (log.isDebugEnabled())
      log.debug("Broadcast alert data");

    messageSender.send(ROUTE_INFO_FROM_SCB_BRIDGE, alertInfoMapResult.getAlertInfo());
    messageSender.send(ROUTE_SCB_INFO_FROM_SYNC, alertInfoMapResult.getScbAlertInfo());
  }
}
