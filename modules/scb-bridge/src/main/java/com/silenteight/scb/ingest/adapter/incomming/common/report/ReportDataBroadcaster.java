package com.silenteight.scb.ingest.adapter.incomming.common.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.ingest.IngestServiceListener;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoMapper;
import com.silenteight.sep.base.common.messaging.MessageSender;

@Slf4j
@RequiredArgsConstructor
class ReportDataBroadcaster implements IngestServiceListener {

  private final MessageSender messageSender;
  private final AlertInfoMapper infoMapper;

  public void send(Alert alert) {
    if (log.isDebugEnabled())
      log.debug("Broadcast alert data");
  }
}
