package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.hsbc.bridge.retention.AlertRetentionSender;
import com.silenteight.hsbc.bridge.retention.DataRetentionMessageSender;
import com.silenteight.hsbc.bridge.retention.DataRetentionType;

import one.util.streamex.StreamEx;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
class AlertRetentionMessageSender implements AlertRetentionSender {

  private final AlertRepository alertRepository;
  private final DataRetentionMessageSender messageSender;

  @Override
  @Transactional(readOnly = true)
  public void send(OffsetDateTime expireDate, int chunkSize, DataRetentionType type) {
    var counter = new AtomicInteger(0);
    try (
        var alertEntityStream = alertRepository.findAlertEntityNamesByAlertTimeBefore(expireDate)
    ) {
      StreamEx.of(alertEntityStream)
          .groupRuns((prev, next) -> counter.incrementAndGet() % chunkSize != 0)
          .forEach(chunk -> sendMessageByType(type, chunk));
    }
  }

  private void sendMessageByType(DataRetentionType type, List<String> chunk) {
    log.debug("Sending message of type: {} with alerts count: {}", type, chunk.size());
    if (type == DataRetentionType.PERSONAL_INFO_EXPIRED) {
      var message = PersonalInformationExpired.newBuilder().addAllAlerts(chunk).build();
      messageSender.send(message);
    } else {
      var message = AlertsExpired.newBuilder().addAllAlerts(chunk).build();
      messageSender.send(message);
    }
  }
}
